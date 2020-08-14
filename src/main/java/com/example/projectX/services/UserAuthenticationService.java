package com.example.projectX.services;

import com.example.projectX.dao.UserDao;
import com.example.projectX.models.Company;
import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserAuthenticationService(@Qualifier("postgres") UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.selectUserByUsername(s).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", s)));
    }

    public boolean saveUserStudent(String login, String name, String password, UUID company_id) {
        return userDao.saveUserStudent(login, name, password, company_id);
    }

    public List<ManagementStaff> getAllCompanyManagers(Company company) {
        return userDao.selectAllCompanyManagers(company);
    }

    public Optional<ManagementStaff> getManagerStaffByLogin(String login) {
        return userDao.selectManagementStaffByLogin(login);
    }

    public Optional<UserStudent> getUserStudentByLogin(String login) {
        return userDao.selectUserStudentByLogin(login);
    }

    public Optional<UserTeacher> getUserTeacherByLogin(String login) {
        return userDao.selectUserTeacherByLogin(login);
    }

    public boolean updateManagementStaffById(UUID managerId, ManagementStaff managementStaff) {
        return userDao.updateManagementStaffById(managerId, managementStaff);
    }

    public boolean updateUserTeacherById(UUID teacherId, UserTeacher userTeacher) {
        return userDao.updateUserTeacherById(teacherId, userTeacher);
    }

    public boolean updateUserStudentById(UUID studentId, UserStudent userStudent) {
        return userDao.updateUserStudentById(studentId, userStudent);
    }
}
