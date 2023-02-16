package com.example.rpc.Server;

import com.example.rpc.Server.netty.NettyRPCserver;
import com.example.rpc.Server.socket.SimpleRPCserver;
import com.example.rpc.Service.BlogServiceImp;
import com.example.rpc.Service.UserServiceImpl;
import com.example.rpc.Utils.ServiceProvider;


public class Server {

    private final static int PORT = 8008;
    private final static String IP = "192.168.137.1";

    public static void main(String[] args) {
        BlogServiceImp blogServiceImp = new BlogServiceImp();
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        ServiceProvider provider = new ServiceProvider(IP, PORT);
        provider.setInterfaceProvider(blogServiceImp);
        provider.setInterfaceProvider(userServiceImpl);


        //rpc服务只能同时开启一个，否则会造成底层传输方式错误报错
        SimpleRPCserver rpcserver = new SimpleRPCserver(provider);
        //rpcserver.start();

        NettyRPCserver nettyRPCserver = new NettyRPCserver(provider);
        nettyRPCserver.start();


    }
}
