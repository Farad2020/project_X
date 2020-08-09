package com.example.projectX.services;

import com.example.projectX.dao.UserDao;
import com.example.projectX.models.Company;
import com.example.projectX.models.ManagementStaff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
