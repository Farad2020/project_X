package com.example.projectX.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class ManagementStaff implements UserDetails {

    private final UUID id;
    private final String name;
    private final String surname;
    private final String lastname;
    private final String login;
    private final String password;
    private final String email;
    private final String telephone;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;
    private final UUID companyId;
    private final int role;
    private final boolean isAbleToDeleteManager;
    private final boolean isAbleToDeleteTeacher;
    private final boolean isAbleToDeleteStudent;
    private final boolean isAbleToAddManager;
    private final boolean isAbleToAddTeacher;
    private final boolean isAbleToAddStudent;
    private final boolean isAbleToDeleteCourse;
    private final boolean isAbleToAddCourse;

    public ManagementStaff(UUID id,
                           String name,
                           String surname,
                           String lastname,
                           String login,
                           String password,
                           String email,
                           String telephone,
                           boolean isAccountNonExpired,
                           boolean isAccountNonLocked,
                           boolean isCredentialsNonExpired,
                           boolean isEnabled,
                           UUID companyId,
                           int role,
                           boolean isAbleToDeleteManager,
                           boolean isAbleToDeleteTeacher,
                           boolean isAbleToDeleteStudent,
                           boolean isAbleToAddManager,
                           boolean isAbleToAddTeacher,
                           boolean isAbleToAddStudent,
                           boolean isAbleToDeleteCourse,
                           boolean isAbleToAddCourse) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.lastname = lastname;
        this.login = login;
        this.password = password;
        this.email = email;
        this.telephone = telephone;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
        this.companyId = companyId;
        this.role = role;
        this.isAbleToDeleteManager = isAbleToDeleteManager;
        this.isAbleToDeleteTeacher = isAbleToDeleteTeacher;
        this.isAbleToDeleteStudent = isAbleToDeleteStudent;
        this.isAbleToAddManager = isAbleToAddManager;
        this.isAbleToAddTeacher = isAbleToAddTeacher;
        this.isAbleToAddStudent = isAbleToAddStudent;
        this.isAbleToDeleteCourse = isAbleToDeleteCourse;
        this.isAbleToAddCourse = isAbleToAddCourse;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public int getRole() {
        return role;
    }

    public boolean isAbleToDeleteManager() {
        return isAbleToDeleteManager;
    }

    public boolean isAbleToDeleteTeacher() {
        return isAbleToDeleteTeacher;
    }

    public boolean isAbleToDeleteStudent() {
        return isAbleToDeleteStudent;
    }

    public boolean isAbleToAddManager() {
        return isAbleToAddManager;
    }

    public boolean isAbleToAddTeacher() {
        return isAbleToAddTeacher;
    }

    public boolean isAbleToAddStudent() {
        return isAbleToAddStudent;
    }

    public boolean isAbleToDeleteCourse() {
        return isAbleToDeleteCourse;
    }

    public boolean isAbleToAddCourse() {
        return isAbleToAddCourse;
    }
}
