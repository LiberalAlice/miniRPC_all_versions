package com.example.rpc.Service;

import com.example.rpc.entity.User;

public interface UserService {

    User getUserById(int id);

    int insertUser(User user);
}
