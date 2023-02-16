package com.example.rpc.Client;

import com.example.rpc.Client.netty.NettyRPCclient;
import com.example.rpc.Client.socket.SocketRPCclient;
import com.example.rpc.entity.Request;
import com.example.rpc.entity.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ClientProxy implements InvocationHandler {

    private RPCClient RPCClient;

    public ClientProxy(SocketRPCclient socketRPCclient) {
        this.RPCClient = socketRPCclient;
    }

    public ClientProxy(NettyRPCclient nettyRPCclient) {
        this.RPCClient = nettyRPCclient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Request request = Request.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName()).params(args)
                .paramsTypes(method.getParameterTypes()).build();

        Response response = RPCClient.sendRequest(request);
        return response.getData();
    }

    <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }


}
