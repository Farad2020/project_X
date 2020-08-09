package com.example.projectX.dao;

import com.example.projectX.models.Company;
import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.User;
import com.example.projectX.models.UserStudent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    Optional<User> selectUserByUsername(String username);

    Optional<UserStudent> selectUserStudentByLogin(String login);

    boolean saveUserStudent(String login, String name, String password, UUID companyId);

    List<ManagementStaff> selectAllCompanyManagers(Company company);

}
