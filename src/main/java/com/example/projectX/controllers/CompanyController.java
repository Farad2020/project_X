package com.example.projectX.controllers;

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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class CompanyController {
    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public CompanyController(CompanyService companyService, UserAuthenticationService userAuthenticationService) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }


    //Компании

    @GetMapping("/company_home")
    public String companyHome(Model model) {
        return "company-home";
    }

    @GetMapping("company_info")
    public String companyInfo(Model model,
                              @AuthenticationPrincipal ManagementStaff manager) {
        Company company = companyService.getCompanyById(manager.getCompanyId()).get();
        //Posts
        model.addAttribute("company", company );
        return "company-info-page";
    }

    @GetMapping("company_management_stuff")
    public String companyManagementStuff(Model model,
                                         @AuthenticationPrincipal ManagementStaff manager) {
        List<ManagementStaff> managementStaffs = companyService.selectAllCompanyManagers( companyService.getCompanyById(manager.getCompanyId()).get() );

        model.addAttribute("managementStaffs", managementStaffs );

        return "company-management-staff";
    }

    @GetMapping("company_posts")
    public String companyPosts(Model model) {
        return "company-posts-page";
    }


    @GetMapping("company_students")
    public String companyStudents(Model model,
                                  @AuthenticationPrincipal ManagementStaff manager) {
        List<UserStudent> students = companyService.getAllCompanyStudents(manager.getCompanyId());
        //Sorting by name currently; Later variations should be added!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Comparator<UserStudent> compareByName = Comparator.comparing(UserStudent::getName);
        Collections.sort(students, compareByName);

        model.addAttribute("students", students );
        return "company-students";
    }

    /*
        @GetMapping("company_students/{sort_type}")
    public String companyStudents(Model model,
                                  @PathVariable (name = "sort_type") String sortType,
                                  @AuthenticationPrincipal ManagementStaff manager) {
        List<UserStudent> students = companyService.getAllCompanyStudents(manager.getCompanyId());
        //Sorting by name currently; Later variations should be added!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Comparator<UserStudent> compareByName = Comparator.comparing(UserStudent::getName);

        if(sortType.equals("alp_asc") ){

            Collections.sort(students, compareByName);

        }else if(sortType.equals("alp_desc") ){

            Collections.sort(students, compareByName.reversed());

        }
        model.addAttribute("students", students );
        return "company-students";
    }
    */

    @GetMapping("company_students_filtered")
    public String companyStudentsFiltered(Model model,
                                  @AuthenticationPrincipal ManagementStaff manager) {
        List<UserStudent> students = companyService.getAllCompanyStudents(manager.getCompanyId());

        model.addAttribute("students", students );
        return "company-students-filtered";
    }

    /*
    Заполнить инфой все темплейты
    Настоящие ссылки и страницы для Курсов, Учителей, Студентов, Компаний

    На последок:
    ДжиЭС: Пагинация, Динамичный поиск, И не гейский фильтр!
    */

    @GetMapping("company_teachers")
    public String companyTeachers(Model model,
                                  @AuthenticationPrincipal ManagementStaff manager) {

        List<UserTeacher> teachers = companyService.getAllCompanyTeachers(manager.getCompanyId());

        model.addAttribute("teachers", teachers );
        return "company-teachers";
    }

    @GetMapping("company_courses")
    public String companyCourses(Model model,
                                 @AuthenticationPrincipal ManagementStaff manager) {

        List<Course> courses = companyService.getAllCompanyCourses(manager.getCompanyId());

        model.addAttribute("courses", courses );
        return "company-courses";
    }

    @GetMapping("company_courses/{course_id}")
    public String getCompanyCourseById(Model model,
                                 @PathVariable (name = "course_id") UUID course_id,
                                 @AuthenticationPrincipal ManagementStaff manager) {

        Course course = companyService.getCourseById(manager.getCompanyId()).get();
        UserTeacher teacher = companyService.getTeacherById(course.getTeacherId()).get();

        model.addAttribute("course", course );
        model.addAttribute("teacher", teacher );
        return "company-course_page";
    }

    @GetMapping("/students_profile/{student_id}")
    public String getUserProfile(Model model,
                              @PathVariable (name = "student_id") UUID student_id,
                              @AuthenticationPrincipal User user){
        UserStudent student = companyService.getStudentById(student_id).get();
        model.addAttribute("student", student );

        /* if student is not the current user validation!!! And user is manager */
        return "student-account-page";
    }

    @GetMapping("/teachers_profile/{teacher_id}")
    public String getTeacherProfile(Model model,
                              @PathVariable (name = "teacher_id") UUID teacher_id,
                              @AuthenticationPrincipal UserDetails user){
        UserTeacher teacher = companyService.getTeacherById(teacher_id).get();
        model.addAttribute("teacher", teacher );
        return "teacher-account-page";
    }

}
