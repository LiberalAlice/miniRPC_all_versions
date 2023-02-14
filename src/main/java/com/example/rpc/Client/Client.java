package com.example.rpc.Client;


import com.example.rpc.Client.netty.NettyRPCclient;
import com.example.rpc.Client.socket.SocketRPCclient;
import com.example.rpc.Service.BlogService;
import com.example.rpc.Service.UserService;
import com.example.rpc.entity.User;

public class Client {

    public static void main(String[] args) {

        //SocketRPCclient socketRPCclient = new SocketRPCclient();
        //ClientProxy clientProxy = new ClientProxy(socketRPCclient);
        //UserService proxy1 = clientProxy.getProxy(UserService.class);
        //BlogService proxy2 = clientProxy.getProxy(BlogService.class);
        //
        //User userById = proxy1.getUserById(1);
        //System.out.println(userById);
        //
        //
        //int i = proxy1.insertUser(new User());
        //System.out.println(i);
        //
        //Boolean url = proxy2.getUrl(1);
        //System.out.println(url);

        //netty test
        NettyRPCclient nettyRPCclient = new NettyRPCclient();
        ClientProxy clientProxy1 = new ClientProxy(nettyRPCclient);
        UserService proxy3 = clientProxy1.getProxy(UserService.class);


        User userById = proxy3.getUserById(1);
        System.out.println("zzzzzzzzzzzzzzzz");
        System.out.println(userById);

        int user = proxy3.insertUser(new User(2, "z", "aaa"));
        System.out.println(user);
    }
}
