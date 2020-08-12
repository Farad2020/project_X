package com.example.projectX.security;

import com.example.projectX.services.AdminAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(1)
public class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AdminAuthenticationService adminAuthenticationService;

    @Autowired
    public AdminSecurityConfig(PasswordEncoder passwordEncoder, AdminAuthenticationService adminAuthenticationService) {
        this.passwordEncoder = passwordEncoder;
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .antMatcher("/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/**")
                .authorizeRequests().anyRequest().authenticated()
                .and().formLogin().loginPage("/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/login")
                    .defaultSuccessUrl("/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/", true)
                .permitAll()
                .and().logout().logoutUrl("/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/logout").
                    logoutSuccessUrl("/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAdminAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAdminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(adminAuthenticationService);
        return provider;
    }
}
