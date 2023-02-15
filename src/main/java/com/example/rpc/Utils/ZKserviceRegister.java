package com.example.rpc.Utils;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        System.out.println("进入服务注册" + serviceNamePath + ".................");
        System.out.println();
        try {
            //// serviceName创建成永久节点，服务提供者下线时，不删服务名，只删地址
            //if(client.checkExists().forPath("/" + serviceName) == null){
            //    client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/" + serviceName);
            //}
            //// 路径地址，一个/代表一个节点
            //String path = "/" + serviceName +"/"+ getServiceAddress(serverAddress);
            //// 临时节点，服务器下线就删除节点
            //client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);


            if (client.checkExists().forPath(serviceNamePath) == null) {
                client.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(serviceNamePath);
            }
            String path = "/" + serviceName + "/" + serverAddress;
            System.out.println(path);
            // 临时节点，服务器下线就删除节点
            client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            System.out.println(serviceName + "服务已注册到zk");

        } catch (Exception e) {
            System.out.println(serviceName + "服务注册到zk失败。。。。。。。。。。。。。。");
            log.error(e.getMessage());
        }
    }

    @Override
    public List<String> serviceDiscovery(String serviceName) {
        System.out.println("进入服务发现。。。。。。。。。。");
        String servicePath = "/" + serviceName;
        try {
            if (client.checkExists().forPath(servicePath) != null) {
                System.out.println("路径不为空。，。。。。。。。。。。。。。。。。");
                List<String> strings = client.getChildren().forPath(servicePath);
                System.out.println(strings.get(0) + "路径的ip地址和port ");
                return strings;
            }
            System.out.println("路径为空。，。。。。。。。。。。。。。。。。");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
