package com.example.rpc.Service;

import com.example.rpc.entity.User;

public class userServiceImp implements UserService{

    public User getUserById(int id) {
        User user = User.builder().Id(id).sex("dafult").name("wuwei").build();
        return user;
    }

    public int insertUser(User user) {
        System.out.println("insert successfully");
        return 1;
    }


}
