package com.example.projectX.services;

import com.example.projectX.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserAuthenticationService(@Qualifier("user_auth") UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.selectUserByUsername(s).orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", s)));
    }
}
