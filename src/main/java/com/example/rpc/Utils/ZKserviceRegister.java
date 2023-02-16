package com.example.rpc.Utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * @Author：wuwei
 * @name：ZKserviceRegister
 * @Date：2023/2/15
 */
public class ZKserviceRegister implements ServiceRegister {

    private static final Logger log = LoggerFactory.getLogger(ZKserviceRegister.class);

    //节点根路径
    private static final String ROOT_PATH = "MyRpc";
    private static final String ZK_SERVER = "192.168.137.1:2181";
    //负载均衡方案，随机，轮询
    private static final String LOAD_BALANCE_RANDOM_POLICY = "random";
    private static final String LOAD_BALANCE_QUERY_POLICY = "query";
    private static String LOAD_BALANCE_POLICY = LOAD_BALANCE_RANDOM_POLICY;
    //服务地址数，切换方案使用
    private static final int LOAD_BALANCE_SERVICE_COUNT = 10;
    //轮询记录数
    private static int choose = -1;

    //zk的节点对象
    private CuratorFramework client;


    public ZKserviceRegister() {
        //重试次数和时间
        ExponentialBackoffRetry retry = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder()
                .connectString(ZK_SERVER)
                .sessionTimeoutMs(10000)
                .namespace(ROOT_PATH)
                .retryPolicy(retry)
                .build();
        this.client.start();
        log.info("zk client 启动成功");
    }

    @Override
    public void register(String serviceName, String serverAddress) {
        String serviceNamePath = "/" + serviceName;
        try {
            //永久节点
            if (client.checkExists().forPath(serviceNamePath) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(serviceNamePath);
            }
            String path = "/" + serviceName + "/" + serverAddress;
            // 临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            System.out.println(serviceName + "服务已注册到zk");

        } catch (Exception e) {
            log.warn(serviceName + "服务注册到zk失败。。。。。。。。。。。。。。");
            log.error(e.getMessage());
        }
    }

    @Override
    public String serviceDiscovery(String serviceName) {
        String servicePath = "/" + serviceName;
        try {
            if (client.checkExists().forPath(servicePath) != null) {
                List<String> strings = client.getChildren().forPath(servicePath);
                String url = selectUrls(strings);
                return url;
            }
            log.warn("路径为空。，。。。。。。。。。。。。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //负载均衡选择url
    private String selectUrls(List<String> strings) {
        switch (LOAD_BALANCE_POLICY) {
            case LOAD_BALANCE_RANDOM_POLICY:
                Random random = new Random();
                String url = strings.get(random.nextInt(strings.size()));
                return url;
            case LOAD_BALANCE_QUERY_POLICY:
                choose++;
                choose = choose % strings.size();
                return strings.get(choose);
            default:
                log.error("负载方案未定义");
                return strings.get(0);
        }
    }

    //监听服务地址的变动，当地址少于一定时改变负载均衡方案
    @Override
    public void registerWatch(String servicePath) {
        CuratorCache curatorCache = CuratorCache.build(client, servicePath);
        CuratorCacheListener listener = CuratorCacheListener.builder().forPathChildrenCache(servicePath, client, new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                int size = client.getChildren().forPath(servicePath).size();
                if (size > LOAD_BALANCE_SERVICE_COUNT) {
                    LOAD_BALANCE_POLICY = LOAD_BALANCE_RANDOM_POLICY;
                    log.info("服务发生变动，当前负载均衡方案为随机");
                } else {
                    LOAD_BALANCE_POLICY = LOAD_BALANCE_QUERY_POLICY;
                    log.info("服务发生变动，当前负载均衡方案为轮询");
                }
            }
        }).build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

}
