package com.example.rpc.Client.netty;

import com.example.rpc.Client.RPCClient;
import com.example.rpc.Client.socket.SocketRPCclient;
import com.example.rpc.Utils.ServiceRegister;
import com.example.rpc.Utils.ZKserviceRegister;
import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Author：wuwei
 * @name：nettyRPCClient
 * @Date：2022/12/12 15:05
 */
public class NettyRPCclient implements RPCClient {

    private String ip;
    private int port;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private static final Logger log = LoggerFactory.getLogger(SocketRPCclient.class);

    private ServiceRegister serviceRegister;

    public NettyRPCclient() {
        // 初始化注册中心，建立连接
        this.serviceRegister = new ZKserviceRegister();
        log.info("建立连接成功。。。。。。。。。");
    }

    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());

    }


    @Override
    public Response sendRequest(Request request) {
        List<String> urls = serviceRegister.serviceDiscovery(request.getInterfaceName());
        String url = urls.get(0);
        int index = url.indexOf(":");
        ip = url.substring(0, index);
        port = Integer.parseInt(url.substring(index + 1));


        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            log.info("netty发送数据成功。。。。。");
            channel.closeFuture().sync();
            AttributeKey<Response> value = AttributeKey.valueOf("Response");
            Response response = channel.attr(value).get();
            if (response == null) {
                log.warn("Response is null..........");
            }
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.warn("返回结果读取失败");
        }
        return null;
    }
}
