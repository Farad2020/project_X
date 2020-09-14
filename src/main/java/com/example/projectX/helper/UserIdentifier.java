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
            model.addAttribute("current_user", (UserStudent)user);
            model.addAttribute("current_user_id", ((UserStudent) user).getId());
            model.addAttribute("current_user_company_id", ((UserStudent) user).getCompanyId());
        }else if( userAuthenticationService.getManagerStaffByLogin(user.getUsername()).isPresent() ){
            model.addAttribute("isManagementStaff", true);
            model.addAttribute("current_user", (ManagementStaff)user);
            model.addAttribute("current_user_id", ((ManagementStaff) user).getId());
            model.addAttribute("current_user_company_id", ((ManagementStaff) user).getCompanyId());
        }else if( userAuthenticationService.getUserTeacherByLogin(user.getUsername()).isPresent() ){
            model.addAttribute("isTeacher", true);
            model.addAttribute("current_user", (UserTeacher)user);
            model.addAttribute("current_user_id", ((UserTeacher)user).getId());
            model.addAttribute("current_user_company_id", ((UserTeacher) user).getCompanyId());
        }
    }
}
