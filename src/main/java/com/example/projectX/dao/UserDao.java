package com.example.projectX.dao;

import com.example.projectX.models.User;
import com.example.projectX.models.UserStudent;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    Optional<User> selectUserByUsername(String username);

    Optional<UserStudent> selectUserStudentByLogin(String login);

    boolean saveUserStudent(String login, String name, String password, UUID companyId);

}
