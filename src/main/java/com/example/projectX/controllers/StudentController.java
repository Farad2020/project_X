package com.example.projectX.controllers;

import com.example.projectX.helper.UserIdentifier;
import com.example.projectX.models.*;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class StudentController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserIdentifier userIdentifier;

    @Autowired
    public StudentController(CompanyService companyService, UserAuthenticationService userAuthenticationService, UserIdentifier userIdentifier) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
        this.userIdentifier = userIdentifier;
    }

    @GetMapping("/student_profile/{student_id}")
    public String differentStudentProfile(Model model,
                                          @PathVariable(name = "student_id") UUID student_id,
                                          @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user,model);
        if( model.getAttribute("isStudent") != null){
            if( ((UserStudent)user).getId().equals(student_id) ){
                model.addAttribute("isOwner", true);
                model.addAttribute("student", (UserStudent)user);
            }else{
                model.addAttribute("student", companyService.getStudentById(student_id).get());
            }
            return "student-account-page";
        }else if( model.getAttribute("isManagementStaff") != null || model.getAttribute("isTeacher") != null){
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

    /* !!!!!!!!!! A BIT OF REDESIGN FOR PERSONAL COURSE PAGES WILL REQUIRED !!!!!!!!!  */
    @GetMapping("student_courses/{course_id}")
    public String getStudentCourseById(Model model,
                                       @PathVariable(name = "course_id") UUID course_id,
                                       @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user,model);
        if( model.getAttribute("isStudent") != null  ){

            Optional<Course> course = companyService.getCourseById(course_id);
            if (course.isPresent() && course.get().getCompanyId().equals( ((UserStudent) user).getCompanyId() ) ) {

                Map<Integer, List<Schedule>> scheduleMap = companyService.getMappedCourseSchedule(course_id);
                model.addAttribute("course", course.get());
                Optional<UserTeacher> teacher = companyService.getTeacherById(course.get().getTeacherId());
                teacher.ifPresent(userTeacher -> {
                    model.addAttribute("teacher", userTeacher);
                    model.addAttribute("company_id", userTeacher.getCompanyId());
                });
                model.addAttribute("schedule_map", scheduleMap);
                return "company-course-page";
            }
        }
         return "error-page";
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
    public String userAttendance(Model model) {
        return "student-attendance-journal";
    }

    @GetMapping("student_tasks")
    public String userTasks(Model model) {
        return "student-tasks-page";
    }

}
