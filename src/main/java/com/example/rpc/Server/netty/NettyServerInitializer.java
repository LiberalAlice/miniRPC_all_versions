package com.example.rpc.Server.netty;

import com.example.rpc.Server.netty.NettyRPChandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Map;

/**
 * @Author：wuwei
 * @name：NettyServerInitializer
 * @Date：2023/2/14
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private Map<String, Object> serviceProvider;

    public NettyServerInitializer(Map<String, Object> serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        System.out.println("netty初始化中。。。。。。。。。");
        ChannelPipeline pipeline = sc.pipeline();
        // 消息格式 [长度][消息体], 解决粘包问题
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        // 计算当前待发送消息的长度，写入到前4个字节中
        pipeline.addLast(new LengthFieldPrepender(4));
        //使用java的序列化
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(new ClassResolver() {
            @Override
            public Class<?> resolve(String classname) throws ClassNotFoundException {
                return Class.forName(classname);
            }
        }));

        pipeline.addLast(new NettyRPChandler(serviceProvider));
        System.out.println("netty初始化完成。。。。。。。。。");
    }
}
