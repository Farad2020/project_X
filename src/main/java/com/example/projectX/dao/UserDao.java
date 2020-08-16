package com.example.projectX.dao;

import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    Optional<? extends UserDetails> selectUserByUsername(String username);

    Optional<UserStudent> selectUserStudentByLogin(String login);

    Optional<ManagementStaff> selectManagementStaffByLogin(String login);

    Optional<UserTeacher> selectUserTeacherByLogin(String login);

    boolean saveUserStudent(String login, String name, String password, UUID companyId);

    boolean updateManagementStaffById(UUID managerId, ManagementStaff managementStaff);

    boolean updateUserTeacherById(UUID teacherId, UserTeacher userTeacher);

    boolean updateUserStudentById(UUID studentId, UserStudent userStudent);

}
