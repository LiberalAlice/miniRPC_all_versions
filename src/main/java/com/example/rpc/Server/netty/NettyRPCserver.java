package com.example.rpc.Server.netty;

import com.example.rpc.Server.RPCserver;
import com.example.rpc.Server.netty.NettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Map;

/**
 * @Author：wuwei
 * @name：NettyRPCserver
 * @Date：2023/2/14
 */
public class NettyRPCserver implements RPCserver {

    private Map<String, Object> serviceProvider;

    public NettyRPCserver(Map<String, Object> serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void start(int port) {
        NioEventLoopGroup boss = new NioEventLoopGroup();

        NioEventLoopGroup worker = new NioEventLoopGroup();
        System.out.println("netty启动。。。。。。。。。。。。。");
        // 启动netty服务器
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 2、创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector
        serverBootstrap.group(boss, worker)
                // 3、选择服务器的 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4、child 负责处理读写，该方法决定了 child 执行哪些操作
                .childHandler(new NettyServerInitializer(serviceProvider));
        try {
            System.out.println("绑定端口。。。。。。。。。。");
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("同步阻塞中。。。。。。。。。。");
            future.channel().closeFuture().sync();
            System.out.println("同步成功。。。。。。。。。。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    @Override
    public void stop() {

    }
}
