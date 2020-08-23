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
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/")
public class CompanyController {
    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;
    private final UserIdentifier userIdentifier;

    @Autowired
    public CompanyController(CompanyService companyService, UserAuthenticationService userAuthenticationService, UserIdentifier userIdentifier) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
        this.userIdentifier = userIdentifier;
    }
    //Компании

    @GetMapping("/company_home")
    public String companyHome(Model model,
                              @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            return "company-home";
        }
        return "error-page";
    }

    @GetMapping("company_info")
    public String companyInfo(Model model,
                              @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            Optional<Company> company = companyService.getCompanyById(((ManagementStaff) user).getCompanyId());
            if (company.isPresent()) {
                //TODO get posts from DB
                model.addAttribute("company", company.get());
            }
            return "company-info-page";
        }
        return "error-page";
    }

    @GetMapping("company_management_stuff")
    public String companyManagementStuff(Model model,
                                         @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            Optional<Company> company = companyService.getCompanyById(((ManagementStaff) user).getCompanyId());
            if (company.isPresent()) {
                //TODO get posts from DB
                List<ManagementStaff> managementStaffs = companyService.selectAllCompanyManagers(company.get());
                model.addAttribute("managementStaffs", managementStaffs );
                model.addAttribute("company", company.get());
            }
            return "company-management-staff";
        }
        return "error-page";
    }

    @GetMapping("company_posts")
    public String companyPosts(Model model,
                               @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            //TODO get posts from DB
            return "company-posts-page";
        }
        return "error-page";
    }


    @GetMapping("company_students")
    public String companyStudents(Model model,
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            List<UserStudent> students = companyService.getAllCompanyStudents(((ManagementStaff)user).getCompanyId());
            //Sorting by name currently; Later variations should be added!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Comparator<UserStudent> compareByName = Comparator.comparing(UserStudent::getName);
            students.sort(compareByName);
            model.addAttribute("students", students );
            return "company-students";
        }
        return "error-page";
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
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            List<UserStudent> students = companyService.getAllCompanyStudents(((ManagementStaff)user).getCompanyId());
            model.addAttribute("students", students );
            return "company-students-filtered";
        }
        return "error-page";
    }

    /*
    Заполнить инфой все темплейты
    Настоящие ссылки и страницы для Курсов, Учителей, Студентов, Компаний

    На последок:
    ДжиЭС: Пагинация, Динамичный поиск, И не гейский фильтр!
    */

    @GetMapping("company_teachers")
    public String companyTeachers(Model model,
                                  @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            List<UserTeacher> teachers = companyService.getAllCompanyTeachers(((ManagementStaff)user).getCompanyId());
            model.addAttribute("teachers", teachers );
            return "company-teachers";
        }
        return "error-page";
    }

    @GetMapping("company_courses")
    public String companyCourses(Model model,
                                 @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            List<Course> courses = companyService.getAllCompanyCourses(((ManagementStaff)user).getCompanyId());
            model.addAttribute("courses", courses );
            return "company-courses";
        }
        return "error-page";
    }

    @GetMapping("company_courses/{course_id}")
    public String getCompanyCourseById(Model model,
                                 @PathVariable (name = "course_id") UUID course_id,
                                 @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            Optional<Course> course = companyService.getCourseById(course_id);
            if (course.isPresent()) {
                Optional<UserTeacher> userTeacher = companyService.getTeacherById(course.get().getTeacherId());
                Map<Integer, List<Schedule>> scheduleMap = companyService.getMappedCourseSchedule(course_id);
                model.addAttribute("course", course.get());
                userTeacher.ifPresent(teacher -> {
                    model.addAttribute("teacher", teacher);
                    model.addAttribute("company_id", teacher.getCompanyId());
                });
                model.addAttribute("schedule_map", scheduleMap);
                return "company-course-page";
            }
        }
        return "error-page";
    }

    @GetMapping("/teachers_profile/{teacher_id}")
    public String getTeacherProfile(Model model,
                              @PathVariable (name = "teacher_id") UUID teacher_id,
                              @AuthenticationPrincipal UserDetails user){
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            Optional<UserTeacher> teacher = companyService.getTeacherById(teacher_id);
            teacher.ifPresent(userTeacher -> {
                model.addAttribute("teacher", userTeacher);
                model.addAttribute("courses", companyService.getAllTeacherCourses(userTeacher.getId()));
            });
            return "teacher-account-page";
        }
        return "error-page";
    }

    @GetMapping("/company_management_staff/{management_staff_id}")
    public String getManagementStaffProfile(Model model,
                                            @PathVariable(name = "management_staff_id") UUID managementStaffId,
                                            @AuthenticationPrincipal UserDetails user) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            Optional<ManagementStaff> managementStaff = companyService.getManagerById(managementStaffId);
            managementStaff.ifPresent(staff -> model.addAttribute("manager", staff));
            return "management-staff-page-for-one";
        }
        return "error-page";
    }

    @GetMapping("/company_courses/{course_id}/delete_schedule/{schedule_id}")
    public String deleteScheduleFromCourse(@PathVariable(name = "course_id") UUID courseId,
                                           @PathVariable(name = "schedule_id") UUID scheduleId,
                                           @AuthenticationPrincipal UserDetails user,
                                           Model model) {
        userIdentifier.getUserClass(user, model);
        if (model.getAttribute("isManagementStaff") != null) {
            ManagementStaff managementStaff = (ManagementStaff) user;
            if (managementStaff.getRole() == 1 || managementStaff.isAbleToDeleteSchedule()) {
                companyService.deleteScheduleFromCourse(scheduleId);
            } else {
                model.addAttribute("permission_error", true);
            }
            return "redirect:/company_courses/" + courseId;
        }
        return "error-page";
    }

    @PostMapping("add_schedule_to_course")
    public String addScheduleToCourse(@RequestParam(name = "start_time") String startTime,
                                      @RequestParam(name = "end_time") String endTime,
                                      @RequestParam(name = "week_day") String weekDay,
                                      @RequestParam(name = "course_id") UUID courseId,
                                      @RequestParam(name = "company_id") UUID companyId) {
        Schedule schedule = new Schedule(UUID.randomUUID(), startTime, endTime, weekDay, courseId);
        boolean result = companyService.addScheduleToCourse(schedule, courseId);
        return "redirect:/company_courses/" + courseId;
    }

}
