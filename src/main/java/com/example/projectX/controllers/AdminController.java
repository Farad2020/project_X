package com.example.projectX.controllers;

import com.example.projectX.models.*;
import com.example.projectX.services.CompanyService;
import com.example.projectX.services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/")
public class AdminController {

    private final CompanyService companyService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public AdminController(CompanyService companyService, UserAuthenticationService userAuthenticationService, PasswordEncoder passwordEncoder) {
        this.companyService = companyService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("")
    public String adminHome(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "admin-home-page";
    }

    @GetMapping("login")
    public String getAdminLoginView() {
        return "admin-login-page";
    }

    @PostMapping("add_company")
    public String addCompany(@RequestParam(name = "company_name") String companyName,
                             @RequestParam(name = "company_email") String companyEmail,
                             @RequestParam(name = "company_telephone") String companyTelephone) {
        boolean result= companyService.addCompany(new Company(null, companyName, companyEmail, companyTelephone));
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/";
    }

    @GetMapping(path = "company/{company_id}")
    public String getCompanyUsersView(@PathVariable("company_id") UUID id, Model model) {
        Optional<Company> company = companyService.getCompanyById(id);
        if (company.isPresent()) {
            List<ManagementStaff> managementStaffList = companyService.selectAllCompanyManagers(company.get());
            List<UserStudent> students = companyService.getAllCompanyStudents(id);
            List<UserTeacher> teachers = companyService.getAllCompanyTeachers(id);
            List<Course> courses = companyService.getAllCompanyCourses(id);
            model.addAttribute("managers", managementStaffList);
            model.addAttribute("students", students);
            model.addAttribute("teachers", teachers);
            model.addAttribute("courses", courses);
        }
        model.addAttribute("company_id", id);
        return "admin-company-page";
    }

    @PostMapping("add_manager")
    public String addManager(@RequestParam(name = "login") String managerLogin,
                             @RequestParam(name = "role") int managerRole,
                             @RequestParam(name = "company_id") UUID companyId) {
        boolean result = companyService.addManagerToCompany(managerLogin, managerRole, companyId);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @PostMapping("add_teacher")
    public String addTeacher(@RequestParam(name = "name") String teacherName,
                             @RequestParam(name = "login") String teacherLogin,
                             @RequestParam(name = "company_id") UUID companyId) {
        boolean result = companyService.addTeacherToCompany(teacherName, teacherLogin, companyId);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @PostMapping("add_student")
    public String addStudent(@RequestParam(name = "name") String studentName,
                             @RequestParam(name = "login") String studentLogin,
                             @RequestParam(name = "company_id") UUID companyId) {
        boolean result = userAuthenticationService.saveUserStudent(studentLogin, studentName, "123", companyId);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @PostMapping("add_course")
    public String addCourse(@RequestParam(name = "name") String courseName,
                            @RequestParam(name = "description") String description,
                            @RequestParam(name = "is_active") boolean isActive,
                            @RequestParam(name = "start_date") String startDate,
                            @RequestParam(name = "price") double price,
                            @RequestParam(name = "payout_num") int payoutNum,
                            @RequestParam(name = "teacher_id") UUID teacherId,
                            @RequestParam(name = "company_id") UUID companyId) {
        System.out.println(startDate);
        Course course = new Course(UUID.randomUUID(), courseName, description, isActive, startDate, null, price, payoutNum, teacherId, companyId);
        boolean result = companyService.addCourseToCompany(companyId, course);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @GetMapping(path = "company/{company_id}/manager/{manager_id}/edit")
    public String editManager(@PathVariable("company_id") UUID companyId,
                              @PathVariable("manager_id") UUID managerId,
                              Model model) {
        Optional<Company> company = companyService.getCompanyById(companyId);
        Optional<ManagementStaff> manager = companyService.getManagerById(managerId);
        company.ifPresent(value -> model.addAttribute("company", value));
        manager.ifPresent(managementStaff -> model.addAttribute("manager", managementStaff));
        return "admin-company-manager-edit";
    }

    @GetMapping(path = "company/{company_id}/teacher/{teacher_id}/edit")
    public String editTeacher(@PathVariable("company_id") UUID companyId,
                              @PathVariable("teacher_id") UUID teacherId,
                              Model model) {
        Optional<Company> company = companyService.getCompanyById(companyId);
        Optional<UserTeacher> teacher = companyService.getTeacherById(teacherId);
        company.ifPresent(value -> model.addAttribute("company", value));
        teacher.ifPresent(userTeacher -> model.addAttribute("teacher", userTeacher));
        return "admin-company-teacher-edit";
    }

    @GetMapping(path = "company/{company_id}/student/{student_id}/edit")
    public String editStudent(@PathVariable("company_id") UUID companyId,
                              @PathVariable("student_id") UUID studentId,
                              Model model) {
        Optional<Company> company = companyService.getCompanyById(companyId);
        Optional<UserStudent> student = companyService.getStudentById(studentId);
        company.ifPresent(value -> model.addAttribute("company", value));
        student.ifPresent(userStudent -> model.addAttribute("student", userStudent));
        return "admin-company-student-edit";
    }

    @GetMapping(path = "company/{company_id}/course/{course_id}/edit")
    public String editCourse(@PathVariable("company_id") UUID companyId,
                             @PathVariable("course_id") UUID courseId,
                             Model model) {
        Optional<Course> course = companyService.getCourseById(courseId);
        List<UserStudent> participatingStudents = companyService.getAllStudentsOfCourse(courseId);
        List<UserStudent> notParticipatingStudents = companyService.getAllCompanyStudentsThatNotInCourse(companyId, courseId);
        List<UserTeacher> teachers = companyService.getAllCompanyTeachers(companyId);
        List<Schedule> scheduleList = companyService.getAllCourseSchedule(courseId);
        course.ifPresent(value -> model.addAttribute("course", value));
        model.addAttribute("participating_students", participatingStudents);
        model.addAttribute("not_participating_students", notParticipatingStudents);
        model.addAttribute("teachers", teachers);
        model.addAttribute("scheduleList", scheduleList);
        model.addAttribute("company_id", companyId);
        return "admin-company-course-edit";
    }

    @PostMapping("edit_manager")
    public String editManager(@RequestParam(name = "name") String name,
                              @RequestParam(name = "surname") String surname,
                              @RequestParam(name = "lastname") String lastname,
                              @RequestParam(name = "login") String login,
                              @RequestParam(name = "password") String password,
                              @RequestParam(name = "email") String email,
                              @RequestParam(name = "telephone") String telephone,
                              @RequestParam(name = "is_account_non_expired") boolean isAccountNonExpired,
                              @RequestParam(name = "is_account_non_locked") boolean isAccountNonLocked,
                              @RequestParam(name = "is_credentials_non_expired") boolean isCredentialsNonExpired,
                              @RequestParam(name = "is_enabled") boolean isEnabled,
                              @RequestParam(name = "company_id") UUID companyId,
                              @RequestParam(name = "role") int role,
                              @RequestParam(name = "is_able_to_delete_manager") boolean isAbleToDeleteManager,
                              @RequestParam(name = "is_able_to_delete_teacher") boolean isAbleToDeleteTeacher,
                              @RequestParam(name = "is_able_to_delete_student") boolean isAbleToDeleteStudent,
                              @RequestParam(name = "is_able_to_add_manager") boolean isAbleToAddManager,
                              @RequestParam(name = "is_able_to_add_teacher") boolean isAbleToAddTeacher,
                              @RequestParam(name = "is_able_to_add_student") boolean isAbleToAddStudent,
                              @RequestParam(name = "is_able_to_delete_course") boolean isAbleToDeleteCourse,
                              @RequestParam(name = "is_able_to_add_course") boolean isAbleToAddCourse,
                              @RequestParam(name = "is_able_to_delete_schedule") boolean isAbleToDeleteSchedule,
                              @RequestParam(name = "is_able_to_add_schedule") boolean isAbleToAddSchedule,
                              @RequestParam(name = "is_able_to_edit_manager") boolean isAbleToEditManagement,
                              @RequestParam(name = "is_able_to_edit_teacher") boolean isAbleToEditTeacher,
                              @RequestParam(name = "is_able_to_edit_student") boolean isAbleToEditStudent,
                              @RequestParam(name = "is_able_to_edit_course") boolean isAbleToEditCourse,
                              @RequestParam(name = "is_able_to_edit_schedule") boolean isAbleToEditSchedule,
                              @RequestParam(name = "manager_id") UUID managerId) {
        ManagementStaff managementStaff = new ManagementStaff(managerId, name, surname, lastname, login, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role, isAbleToDeleteManager, isAbleToDeleteTeacher, isAbleToDeleteStudent, isAbleToAddManager, isAbleToAddTeacher, isAbleToAddStudent, isAbleToDeleteCourse, isAbleToAddCourse, isAbleToDeleteSchedule, isAbleToAddSchedule, isAbleToEditManagement, isAbleToEditTeacher, isAbleToEditStudent, isAbleToEditCourse, isAbleToEditSchedule);
        boolean result = userAuthenticationService.updateManagementStaffById(managerId, managementStaff);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @PostMapping("edit_teacher")
    public String editTeacher(@RequestParam(name = "name") String name,
                              @RequestParam(name = "surname") String surname,
                              @RequestParam(name = "lastname") String lastname,
                              @RequestParam(name = "login") String login,
                              @RequestParam(name = "password") String password,
                              @RequestParam(name = "email") String email,
                              @RequestParam(name = "telephone") String telephone,
                              @RequestParam(name = "is_account_non_expired") boolean isAccountNonExpired,
                              @RequestParam(name = "is_account_non_locked") boolean isAccountNonLocked,
                              @RequestParam(name = "is_credentials_non_expired") boolean isCredentialsNonExpired,
                              @RequestParam(name = "is_enabled") boolean isEnabled,
                              @RequestParam(name = "company_id") UUID companyId,
                              @RequestParam(name = "teacher_id") UUID teacherId) {
        UserTeacher userTeacher = new UserTeacher(teacherId, name, surname, lastname, login, password, email, telephone, companyId, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        boolean result = userAuthenticationService.updateUserTeacherById(teacherId, userTeacher);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @PostMapping("edit_student")
    public String editStudent(@RequestParam(name = "name") String name,
                              @RequestParam(name = "surname") String surname,
                              @RequestParam(name = "lastname") String lastname,
                              @RequestParam(name = "login") String login,
                              @RequestParam(name = "password") String password,
                              @RequestParam(name = "email") String email,
                              @RequestParam(name = "telephone") String telephone,
                              @RequestParam(name = "is_account_non_expired") boolean isAccountNonExpired,
                              @RequestParam(name = "is_account_non_locked") boolean isAccountNonLocked,
                              @RequestParam(name = "is_credentials_non_expired") boolean isCredentialsNonExpired,
                              @RequestParam(name = "is_enabled") boolean isEnabled,
                              @RequestParam(name = "company_id") UUID companyId,
                              @RequestParam(name = "profile_image_oid") long profileImageOid,
                              @RequestParam(name = "student_id") UUID studentId) {
        UserStudent userStudent = new UserStudent(studentId, name, surname, lastname, login, password, email, telephone, companyId, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        boolean result = userAuthenticationService.updateUserStudentById(studentId, userStudent);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @PostMapping("edit_course")
    public String editCourse(@RequestParam(name = "name") String name,
                             @RequestParam(name = "description") String description,
                             @RequestParam(name = "is_active") boolean isActive,
                             @RequestParam(name = "start_date") String startDate,
                             @RequestParam(name = "end_date") String endDate,
                             @RequestParam(name = "price") double price,
                             @RequestParam(name = "payout_num") int payoutNum,
                             @RequestParam(name = "teacher_id") UUID teacherId,
                             @RequestParam(name = "company_id") UUID companyId,
                             @RequestParam(name = "course_id") UUID courseId) {
        Course course = new Course(courseId, name, description, isActive, startDate, endDate, price, payoutNum, teacherId, companyId);
        boolean result = companyService.updateCourseById(courseId, course);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId;
    }

    @GetMapping(path = "company/{company_id}/course/{course_id}/add_student/{student_id}")
    public String addStudentToCourse(@PathVariable(name = "company_id") UUID companyId,
                                     @PathVariable(name = "course_id") UUID courseId,
                                     @PathVariable(name = "student_id") UUID studentId) {
        boolean result = companyService.addStudentToCourse(studentId, courseId);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId + "/course/" + courseId + "/edit";
    }

    @GetMapping(path = "company/{company_id}/course/{course_id}/delete_student/{student_id}")
    public String deleteStudentFromCourse(@PathVariable(name = "company_id") UUID companyId,
                                     @PathVariable(name = "course_id") UUID courseId,
                                     @PathVariable(name = "student_id") UUID studentId) {
        boolean result = companyService.deleteStudentFromCourse(studentId, courseId);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId + "/course/" + courseId + "/edit";
    }

    @PostMapping("add_schedule_to_course")
    public String addScheduleToCourse(@RequestParam(name = "start_time") String startTime,
                                      @RequestParam(name = "end_time") String endTime,
                                      @RequestParam(name = "week_day") String weekDay,
                                      @RequestParam(name = "course_id") UUID courseId,
                                      @RequestParam(name = "company_id") UUID companyId) {
        Schedule schedule = new Schedule(UUID.randomUUID(), startTime, endTime, weekDay, courseId);
        boolean result = companyService.addScheduleToCourse(schedule, courseId);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId + "/course/" + courseId + "/edit";
    }

    @GetMapping(path = "company/{company_id}/course/{course_id}/delete_schedule/{schedule_id}")
    public String deleteScheduleFromCourse(@PathVariable(name = "company_id") UUID companyId,
                                           @PathVariable(name = "course_id") UUID courseId,
                                           @PathVariable(name = "schedule_id") UUID scheduleId) {
        boolean result = companyService.deleteScheduleFromCourse(scheduleId);
        System.out.println(result);
        return "redirect:/$2a$10$HZR3IGneje95jJVEomN.vuEKlxwRt6Tn5oeLEXySZXh7L/WLiX6fm/company/" + companyId + "/course/" + courseId + "/edit";
    }
}
