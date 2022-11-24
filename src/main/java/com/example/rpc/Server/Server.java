package com.example.rpc.Server;

import com.example.rpc.Service.blogServiceImp;
import com.example.rpc.Service.userServiceImp;
import com.example.rpc.Utils.ServiceProvider;


public class Server {

    public static void main(String[] args) {
        blogServiceImp blogServiceImp = new blogServiceImp();
        userServiceImp userServiceImp = new userServiceImp();


    }
}
