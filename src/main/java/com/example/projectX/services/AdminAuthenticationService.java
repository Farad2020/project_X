package com.example.projectX.services;

import com.example.projectX.dao.AdminDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthenticationService implements UserDetailsService {

    private final AdminDao adminDao;

    @Autowired
    public AdminAuthenticationService(@Qualifier("postgres") AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return adminDao.selectAdminByLogin(s).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", s)));
    }
}
