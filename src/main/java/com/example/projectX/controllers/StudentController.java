package com.example.projectX.controllers;

import com.example.projectX.helper.UserIdentifier;
import com.example.projectX.models.Course;
import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
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
import java.util.Optional;
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

    @GetMapping("/student_profile")
    public String userProfile(Model model,
                              @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user,model);
        if(model.getAttribute("isStudent") != null) {
            Resource file = mediaFilesService.getStudentProfilePicture((UserStudent) model.getAttribute("student"));
            try {
                if (file != null) {
                    //System.out.println(Arrays.toString(file.getInputStream().readAllBytes()));
                    //model.addAttribute("profile_picture", file.getURL());
                    System.out.println(file.contentLength());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "student-account-page";
        }
        else {
            return "error-page";
        }
    }

    @GetMapping("student_courses")
    public String userCourses(Model model,
                              @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);
        if( (Boolean) model.getAttribute("isStudent")  ){
            List<Course> courses = companyService.getAllStudentCourses(  ((UserStudent) user).getId() );
            model.addAttribute("courses", courses );
            return "student-courses-page";
        }else{
            return "error-page";
        }
    }

    /* !!!!!!!!!! A BIT OF REDESIGN FOR PERSONAL COURSE PAGES WILL REQUIRED !!!!!!!!!  */
    @GetMapping("student_courses/{course_id}")
    public String getStudentCourseById(Model model,
                                       @PathVariable(name = "course_id") UUID course_id,
                                       @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);
        if( (Boolean) model.getAttribute("isStudent")  ){

            Optional<Course> course = companyService.getCourseById(course_id);
            if (course.isPresent() && course.get().getCompanyId().equals( ((UserStudent) user).getCompanyId() ) ) {

                model.addAttribute("course", course.get());
                Optional<UserTeacher> teacher = companyService.getTeacherById(course.get().getTeacherId());
                teacher.ifPresent(userTeacher -> model.addAttribute("teacher", userTeacher));
                return "company-course-page";
            }else{
                return "error-page";
            }
        }else{
            return "error-page";
        }
    }


    @GetMapping("student_teachers")
    public String userTeachers(Model model,
                               @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);

        if( (Boolean) model.getAttribute("isStudent")  ){
            List<UserTeacher> teachers = companyService.getAllStudentTeachers( ((UserStudent) user).getId() );
            model.addAttribute("teachers", teachers );
            return "student-teachers-page";
        }else{
            return "error-page";
        }
    }

    @GetMapping("student_teachers/{teacher_id}")
    public String getStudentTeacherById(Model model,
                               @PathVariable(name = "teacher_id") UUID teacher_id,
                               @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);

        if( model.getAttribute("isStudent") != null ){

            Optional<UserTeacher> teacher = companyService.getTeacherById(teacher_id);

            if (teacher.isPresent() && teacher.get().getCompanyId().equals( ((UserStudent) user).getCompanyId() ) ) {

                model.addAttribute("teacher", teacher.get());
                // courses means all courses related to chosen teacher
                List<Course> courses = companyService.getAllTeacherCourses(teacher_id);
                System.out.println( courses );
                /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! null exception !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  */
                /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  Menu bar dissappeared !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  */
                model.addAttribute("courses", courses);
                return "teacher-account-page";
            }else{
                return "error-page";
            }
        }else{
            return "error-page";
        }
    }


    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  */

    @GetMapping("student_attendance")
    public String userAttendance(Model model) {
        return "student-attendance-journal";
    }

    @GetMapping("student_tasks")
    public String userTasks(Model model) {
        return "student-tasks-page";
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
