package com.example.projectX.dao;

import com.example.projectX.models.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> selectUserByUsername(String username);

}
