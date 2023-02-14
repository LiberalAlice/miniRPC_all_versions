package com.example.rpc.Server;

import com.example.rpc.Server.netty.NettyRPCserver;
import com.example.rpc.Server.socket.SimpleRPCserver;
import com.example.rpc.Service.blogServiceImp;
import com.example.rpc.Service.userServiceImp;
import com.example.rpc.Utils.ServiceProvider;


public class Server {

    private final static int PORT = 8008;

    public static void main(String[] args) {
        blogServiceImp blogServiceImp = new blogServiceImp();
        userServiceImp userServiceImp = new userServiceImp();
        ServiceProvider provider = new ServiceProvider();
        provider.setInterfaceProvider(blogServiceImp);
        provider.setInterfaceProvider(userServiceImp);


        SimpleRPCserver rpcserver = new SimpleRPCserver(provider.getInterfaceProvider());
        //rpcserver.start(PORT);

        NettyRPCserver nettyRPCserver = new NettyRPCserver(provider.getInterfaceProvider());
        nettyRPCserver.start(PORT);


    }
}
