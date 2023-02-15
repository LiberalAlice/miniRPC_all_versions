package com.example.rpc.Service;

import com.example.rpc.entity.User;

/**
 * @Author：wuwei
 * @name：UserServiceImpl
 * @Date：2023/2/14
 */
public class UserServiceImpl implements UserService{

    @Override
    public User getUserById(int id) {
        return User.builder().Id(id).sex("dafult").name("name").build();
    }

    @Override
    public int insertUser(User user) {
        System.out.println("insert successfully");
        return 1;
    }


}
