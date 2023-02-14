package com.example.rpc.Client;


import com.example.rpc.Service.BlogService;
import com.example.rpc.Service.UserService;
import com.example.rpc.entity.User;

public class Client {

    public static void main(String[] args) {

        SocketIOsend socketIOsend = new SocketIOsend();
        ClientProxy clientProxy = new ClientProxy(socketIOsend);
        UserService proxy = clientProxy.getProxy(UserService.class);
        BlogService proxy2 = clientProxy.getProxy(BlogService.class);

        User userById = proxy.getUserById(1);
        System.out.println(userById);


        int i = proxy.insertUser(new User());
        System.out.println(i);

        Boolean url = proxy2.getUrl(1);
        System.out.println(url);

    }
}
