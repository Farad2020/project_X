package com.example.projectX.controllers;

import com.example.projectX.helper.UserIdentifier;
import com.example.projectX.models.*;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.MediaFilesService;
import com.example.projectX.services.UserAuthenticationService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class StudentController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserIdentifier userIdentifier;
    private final MediaFilesService mediaFilesService;

    @Autowired
    public StudentController(CompanyService companyService, UserAuthenticationService userAuthenticationService, UserIdentifier userIdentifier, MediaFilesService mediaFilesService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
        this.userIdentifier = userIdentifier;
        this.mediaFilesService = mediaFilesService;
    }

    @GetMapping("/student_profile/{student_id}")
    public String differentStudentProfile(Model model,
                                          @PathVariable(name = "student_id") UUID student_id,
                                          @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user,model);
        if( model.getAttribute("isStudent") != null && ((UserStudent)user).getCompanyId().equals( companyService.getStudentById(student_id).get().getCompanyId() ) ){
            if( ((UserStudent)user).getId().equals(student_id) ){
                model.addAttribute("isOwner", true);
                model.addAttribute("student", (UserStudent)user);
            }else{
                model.addAttribute("student", companyService.getStudentById(student_id).get());
            }
            return "student-account-page";
        }else if( (model.getAttribute("isManagementStaff") != null &&
                        ((ManagementStaff)user).getCompanyId().equals( companyService.getStudentById(student_id).get().getCompanyId() ) )||
                (model.getAttribute("isTeacher") != null &&
                        ((UserTeacher)user).getCompanyId().equals( companyService.getStudentById(student_id).get().getCompanyId() ) )){
            model.addAttribute("student", companyService.getStudentById(student_id).get());
            return "student-account-page";
        }
        return "error-page";
    }

    @GetMapping("student_courses")
    public String userCourses(Model model,
                              @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);
        if( model.getAttribute("isStudent") != null ){
            List<Course> courses = companyService.getAllStudentCourses(  ((UserStudent) user).getId() );
            model.addAttribute("courses", courses );
            return "student-courses-page";
        }else{
            return "error-page";
        }
    }

    @GetMapping("student_teachers")
    public String userTeachers(Model model,
                               @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);

        if( model.getAttribute("isStudent") != null  ){
            List<UserTeacher> teachers = companyService.getAllStudentTeachers( ((UserStudent) user).getId() );
            model.addAttribute("teachers", teachers );
            return "student-teachers-page";
        }else{
            return "error-page";
        }
    }



    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  */

    @GetMapping("student_schedule/{course_id}")
    public String studentCourseSchedule(Model model,
                               @PathVariable(name = "course_id") UUID course_id,
                               @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);

        if( model.getAttribute("isStudent") != null ){

            List<Course> courses = companyService.getAllStudentCourses( ((UserStudent)user).getId() );
            Course course = companyService.getCourseById(course_id).get();
            Map<Integer, List<Schedule>> scheduleMap = companyService.getMappedCourseSchedule(course_id);
            model.addAttribute("courses", courses);
            model.addAttribute("course", course);
            model.addAttribute("schedule_map", scheduleMap);
            return "student-schedule-page";
        }else{
            return "error-page";
        }
    }

    @GetMapping("student_schedule")
    public String studentFullSchedule(Model model,
                               @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);

        if( model.getAttribute("isStudent") != null ){

            Map<Integer, List<Schedule>> scheduleMap = companyService.getMappedStudentSchedule( ((UserStudent) user).getId() );
            List<Course> courses = companyService.getAllStudentCourses( ((UserStudent) user).getId() );
            model.addAttribute("courses", courses);
            model.addAttribute("schedule_map", scheduleMap);
            return "schedule";
        }else{
            return "error-page";
        }
    }

    @GetMapping("student_attendance")
    public String userAttendance(Model model,
                                 @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user,model);
        if( model.getAttribute("isStudent") != null){
            return "student-attendance-journal";
        }
        return "error-page";
    }

    @GetMapping("student_tasks")
    public String userTasks(Model model,
                            @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user,model);
        if( model.getAttribute("isStudent") != null){
            return "student-tasks-page";
        }
        return "error-page";
    }

    @PostMapping("student_profile/change_profile_picture")
    public String changeProfilePicture(@RequestParam(name = "image") MultipartFile image,
                                       @RequestParam(name = "student_id") UUID studentId) {
        if (Objects.requireNonNull(image.getContentType()).contains("image")) {
            boolean result = mediaFilesService.changeStudentProfilePicture(studentId, image.getResource());
            System.out.println(result);
        }
        return "redirect:/student_profile";
    }

    @GetMapping("/student_profile_picture/{student_id}")
    public void showStudentProfilePicture(@PathVariable(name = "student_id") UUID studentId,
                                          HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        Resource file = mediaFilesService.getStudentProfilePicture(companyService.getStudentById(studentId).get());
        InputStream is = new ByteArrayInputStream(file.getInputStream().readAllBytes());
        IOUtils.copy(is, response.getOutputStream());
    }

}
