package com.example.rpc.Server.netty;

import com.example.rpc.Utils.ServiceProvider;
import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author：wuwei
 * @name：NettyRPChandler
 * @Date：2023/2/14
 */
public class NettyRPChandler extends SimpleChannelInboundHandler<Request> {

    private Map<String, Object> serviceProvider;

    public NettyRPChandler(Map<String, Object> serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        ctx.writeAndFlush(getResponse(request));
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private Response getResponse(Request request) throws InstantiationException {
        Method method = null;
        try {
            Object c = serviceProvider.get(request.getInterfaceName());
            method =  c.getClass().getDeclaredMethod(request.getMethodName(), request.getParamsTypes());
            Object o =  method.invoke(c, request.getParams());
            return Response.success(o);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return Response.fail();
        }

    }


}
