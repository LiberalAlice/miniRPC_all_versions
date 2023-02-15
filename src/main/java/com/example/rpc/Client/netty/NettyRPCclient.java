package com.example.rpc.Client.netty;

import com.example.rpc.Client.RPCClient;
import com.example.rpc.Client.netty.NettyClientHandle;
import com.example.rpc.Client.socket.SocketRPCclient;
import com.example.rpc.Server.netty.NettyServerInitializer;
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

/**
 * @Author：wuwei
 * @name：nettyRPCClient
 * @Date：2022/12/12 15:05
 */
public class NettyRPCclient implements RPCClient {

    private static final String ip = "127.0.0.1";
    private static final int port = 8008;
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup eventLoopGroup;

    private static final Logger log = LoggerFactory.getLogger(SocketRPCclient.class);


    static {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new NettyClientInitializer());

    }


    @Override
    public Response sendRequest(Request request) {
        System.out.println("netty发送数据。。。。。");

        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(request);
            channel.closeFuture().sync();
            AttributeKey<Response> value = AttributeKey.valueOf("Response");
            System.out.println("netty发送数据成功。。。。。" + value);
            Response response = channel.attr(value).get();
            if (response == null) {
                System.out.println("null..........");
            }
            return response;
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.warn("返回结果读取失败");
        }
        return null;
    }
}
