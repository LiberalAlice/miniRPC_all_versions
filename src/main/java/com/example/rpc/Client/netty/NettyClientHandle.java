package com.example.rpc.Client.netty;

import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * @Author：wuwei
 * @name：NettyClientHandle
 * @Date：2023/2/14
 */
public class NettyClientHandle extends SimpleChannelInboundHandler<Response> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Response response) throws Exception {
        AttributeKey<Response> key = AttributeKey.valueOf("RPCResponse");
        ctx.channel().attr(key).set(response);
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
