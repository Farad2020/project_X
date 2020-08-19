package com.example.projectX.helper;

import com.example.projectX.models.ManagementStaff;
import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserIdentifier {
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public UserIdentifier(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    public void getUserClass(UserDetails user, Model model){
        if( userAuthenticationService.getUserStudentByLogin(user.getUsername()).isPresent() ){
            model.addAttribute("isStudent", true);
            model.addAttribute("student", (UserStudent)user);
        }else if( userAuthenticationService.getManagerStaffByLogin(user.getUsername()).isPresent() ){
            model.addAttribute("isManagementStaff", true);
            model.addAttribute("manager", (ManagementStaff)user);
        }else if( userAuthenticationService.getUserTeacherByLogin(user.getUsername()).isPresent() ){
            model.addAttribute("isTeacher", true);
            model.addAttribute("teacher", (UserTeacher)user);
        }
    }
}
