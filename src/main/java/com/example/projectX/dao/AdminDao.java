package com.example.projectX.dao;

import com.example.projectX.models.Admin;

import java.util.Optional;

public interface AdminDao {

    Optional<Admin> selectAdminByLogin(String login);

}
