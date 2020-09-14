package com.example.projectX.dao;

import com.example.projectX.datasource.PostgresDataSource;
import com.example.projectX.models.*;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Repository("postgres")
public class ApplicationDataAccessService implements CompanyDao, UserDao, AdminDao, MediaFilesDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final PostgresDataSource postgresDataSource;

    @Autowired
    public ApplicationDataAccessService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder, PostgresDataSource postgresDataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
        this.postgresDataSource = postgresDataSource;
    }

    @Override
    public boolean insertCompany(UUID id, Company company) {
        if (getCompanyByName(company.getName()).isPresent()) {
            return false;
        }

        final String sql = String.format("INSERT INTO Companies (company_id, company_name, company_email, company_telephone) " +
                        "VALUES ('%s', '%s', '%s', '%s')",
                id.toString(), company.getName(), company.getEmail(), company.getTelephone());
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        final String sql = String.format("SELECT * FROM Companies WHERE company_name = '%s;'", name);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id_ = UUID.fromString(resultSet.getString("company_id"));
            String name_ = resultSet.getString("company_name");
            String email = resultSet.getString("company_email");
            String telephone = resultSet.getString("company_telephone");
            return new Company(id_, name_, email, telephone);
        })).stream().findFirst();
    }

    @Override
    public Optional<Company> getCompanyById(UUID id) {
        final String sql = String.format("SELECT * FROM Companies WHERE company_id = '%s';", id.toString());
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id_ = UUID.fromString(resultSet.getString("company_id"));
            String name = resultSet.getString("company_name");
            String email = resultSet.getString("company_email");
            String telephone = resultSet.getString("company_telephone");
            return new Company(id_, name, email, telephone);
        })).stream().findFirst();
    }

    @Override
    public List<Company> getAllCompanies() {
        final String sql = "SELECT * FROM Companies";

        return jdbcTemplate.query(
                sql,
                ((resultSet, i) -> {
                    UUID id = UUID.fromString(resultSet.getString("company_id"));
                    String name = resultSet.getString("company_name");
                    String email = resultSet.getString("company_email");
                    String telephone = resultSet.getString("company_telephone");
                    return new Company(id, name, email, telephone);
                })
        );
    }

    @Override
    public boolean insertManager(String managerLogin, int role, UUID companyId) {
        if (selectManagementStaffByLogin(managerLogin).isPresent() ||
            selectUserStudentByLogin(managerLogin).isPresent() ||
            selectUserTeacherByLogin(managerLogin).isPresent()) {
            return false;
        }
        final String sql = String.format("INSERT INTO Management_Staff " +
                "(management_staff_id, management_staff_login, management_staff_password, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, company_id, role) " +
                "VALUES (uuid_generate_v4(), '%s', '%s', True, True, True, True, '%s', '%d');",
                managerLogin, passwordEncoder.encode("12345678"), companyId.toString(), role);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean insertTeacher(String teacherName, String teacherLogin, UUID companyId) {
        if (selectUserTeacherByLogin(teacherLogin).isPresent() ||
            selectManagementStaffByLogin(teacherLogin).isPresent() ||
            selectUserStudentByLogin(teacherLogin).isPresent()) {
            return false;
        }
        final String sql = String.format("INSERT INTO User_Teachers " +
                "(user_teacher_id, user_teacher_name, user_teacher_login, user_teacher_password, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, company_id) " +
                "VALUES (uuid_generate_v4(), '%s', '%s', '%s', TRUE, TRUE, TRUE, TRUE, '%s');",
                teacherName, teacherLogin, passwordEncoder.encode("12345678"), companyId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean insertCourse(UUID companyId, Course course) {
        final String sql = String.format("INSERT INTO Courses " +
                        "(course_id, course_name, course_description, course_is_active, course_start_date, course_price, course_payout_num, user_teacher_id, company_id) " +
                        "VALUES ('%s', '%s', '%s', '%s', '%s', %s, %d, '%s', '%s');",
                course.getId(), course.getName(), course.getDescription(), course.isActive(), course.getStartDate(), course.getPriceString(), course.getPayoutNum(), course.getTeacherId(), companyId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public Optional<ManagementStaff> getManagerById(UUID id) {
        final String sql = String.format("SELECT * FROM Management_Staff WHERE management_staff_id = '%s'", id);
        List<ManagementStaff> managers = jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id_ = UUID.fromString(resultSet.getString("management_staff_id"));
            String name = resultSet.getString("management_staff_name");
            String surname = resultSet.getString("management_staff_surname");
            String lastname = resultSet.getString("management_staff_lastname");
            String login_ = resultSet.getString("management_staff_login");
            String password = resultSet.getString("management_staff_password");
            String email = resultSet.getString("management_staff_email");
            String telephone = resultSet.getString("management_staff_telephone");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId = UUID.fromString(resultSet.getString("company_id"));
            int role = resultSet.getInt("role");
            boolean isAbleToDeleteManager = resultSet.getBoolean("is_able_to_delete_manager");
            boolean isAbleToDeleteTeacher = resultSet.getBoolean("is_able_to_delete_teacher");
            boolean isAbleToDeleteStudent = resultSet.getBoolean("is_able_to_delete_student");
            boolean isAbleToAddManager = resultSet.getBoolean("is_able_to_add_manager");
            boolean isAbleToAddTeacher = resultSet.getBoolean("is_able_to_add_teacher");
            boolean isAbleToAddStudent = resultSet.getBoolean("is_able_to_add_student");
            boolean isAbleToDeleteCourse = resultSet.getBoolean("is_able_to_delete_course");
            boolean isAbleToAddCourse = resultSet.getBoolean("is_able_to_add_course");
            boolean isAbleToDeleteSchedule = resultSet.getBoolean("is_able_to_delete_schedule");
            boolean isAbleToAddSchedule = resultSet.getBoolean("is_able_to_add_schedule");
            boolean isAbleToEditManagement = resultSet.getBoolean("is_able_to_edit_manager");
            boolean isAbleToEditTeacher = resultSet.getBoolean("is_able_to_edit_teacher");
            boolean isAbleToEditStudent = resultSet.getBoolean("is_able_to_edit_student");
            boolean isAbleToEditCourse = resultSet.getBoolean("is_able_to_edit_course");
            boolean isAbleToEditSchedule = resultSet.getBoolean("is_able_to_edit_schedule");
            return new ManagementStaff(id_, name, surname, lastname, login_, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role, isAbleToDeleteManager, isAbleToDeleteTeacher, isAbleToDeleteStudent, isAbleToAddManager, isAbleToAddTeacher, isAbleToAddStudent, isAbleToDeleteCourse, isAbleToAddCourse, isAbleToDeleteSchedule, isAbleToAddSchedule, isAbleToEditManagement, isAbleToEditTeacher, isAbleToEditStudent, isAbleToEditCourse, isAbleToEditSchedule);
        }));
        return managers.stream().findFirst();
    }

    @Override
    public Optional<UserTeacher> getTeacherById(UUID teacherId) {
        final String sql = String.format("SELECT * FROM User_Teachers WHERE user_teacher_id = '%s';", teacherId);
        List<UserTeacher> teachers = jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_teacher_id"));
            String name = resultSet.getString("user_teacher_name");
            String surname = resultSet.getString("user_teacher_surname");
            String lastname = resultSet.getString("user_teacher_lastname");
            String login_ = resultSet.getString("user_teacher_login");
            String password = resultSet.getString("user_teacher_password");
            String email = resultSet.getString("user_teacher_email");
            String telephone = resultSet.getString("user_teacher_telephone");
            long profileImageOid = resultSet.getLong("user_teacher_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
        return teachers.stream().findFirst();
    }

    @Override
    public Optional<UserStudent> getStudentById(UUID studentId) {
        final String sql = String.format("SELECT * FROM User_Students WHERE user_student_id = '%s';", studentId);
        List<UserStudent> userStudents = jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_student_id"));
            String name = resultSet.getString("user_student_name");
            String surname = resultSet.getString("user_student_surname");
            String lastname = resultSet.getString("user_student_lastname");
            String login_ = resultSet.getString("user_student_login");
            String password = resultSet.getString("user_student_password");
            String email = resultSet.getString("user_student_email");
            String telephone = resultSet.getString("user_student_telephone");
            UUID companyId = null;
            if (resultSet.getString("company_id") != null) {
                companyId = UUID.fromString(resultSet.getString("company_id"));
            }
            long profileImageOid = resultSet.getLong("user_student_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
        return userStudents.stream().findFirst();
    }

    @Override
    public Optional<Course> getCourseById(UUID courseId) {
        final String sql = String.format("SELECT * FROM Courses WHERE course_id = '%s';", courseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("course_id"));
            String name = resultSet.getString("course_name");
            String description = resultSet.getString("course_description");
            boolean isActive = resultSet.getBoolean("course_is_active");
            String startDate = resultSet.getString("course_start_date");
            String endDate = resultSet.getString("course_end_date");
            double price = resultSet.getDouble("course_price");
            int payoutNum = resultSet.getInt("course_payout_num");
            UUID teacherId = UUID.fromString(resultSet.getString("user_teacher_id"));
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new Course(id, name, description, isActive, startDate, endDate, price, payoutNum, teacherId, companyId_);
        })).stream().findFirst();
    }

    @Override
    public List<UserStudent> getAllCompanyStudents(UUID companyId) {
        final String sql = String.format("SELECT * FROM User_Students WHERE company_id = '%s'", companyId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_student_id"));
            String name = resultSet.getString("user_student_name");
            String surname = resultSet.getString("user_student_surname");
            String lastname = resultSet.getString("user_student_lastname");
            String login_ = resultSet.getString("user_student_login");
            String password = resultSet.getString("user_student_password");
            String email = resultSet.getString("user_student_email");
            String telephone = resultSet.getString("user_student_telephone");
            UUID companyId_ = null;
            if (resultSet.getString("company_id") != null) {
                companyId_ = UUID.fromString(resultSet.getString("company_id"));
            }
            long profileImageOid = resultSet.getLong("user_student_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
    }

    @Override
    public List<UserTeacher> getAllCompanyTeachers(UUID companyId) {
        final String sql = String.format("SELECT * FROM User_Teachers WHERE company_id = '%s'", companyId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_teacher_id"));
            String name = resultSet.getString("user_teacher_name");
            String surname = resultSet.getString("user_teacher_surname");
            String lastname = resultSet.getString("user_teacher_lastname");
            String login_ = resultSet.getString("user_teacher_login");
            String password = resultSet.getString("user_teacher_password");
            String email = resultSet.getString("user_teacher_email");
            String telephone = resultSet.getString("user_teacher_telephone");
            long profileImageOid = resultSet.getLong("user_teacher_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId_, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);

        }));
    }

    @Override
    public List<Course> getAllCompanyCourses(UUID companyId) {
        final String sql = String.format("SELECT * FROM Courses WHERE company_id = '%s';", companyId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("course_id"));
            String name = resultSet.getString("course_name");
            String description = resultSet.getString("course_description");
            boolean isActive = resultSet.getBoolean("course_is_active");
            String startDate = resultSet.getString("course_start_date");
            String endDate = resultSet.getString("course_end_date");
            double price = resultSet.getDouble("course_price");
            int payoutNum = resultSet.getInt("course_payout_num");
            UUID teacherId = UUID.fromString(resultSet.getString("user_teacher_id"));
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new Course(id, name, description, isActive, startDate, endDate, price, payoutNum, teacherId, companyId_);
        }));
    }

    @Override
    public List<UserStudent> getAllStudentsOfCourse(UUID courseId) {
        final String sql = String.format("SELECT * FROM User_Students " +
                "WHERE user_student_id = ANY (" +
                "SELECT student_id FROM Students_Courses " +
                "WHERE course_id = '%s');", courseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_student_id"));
            String name = resultSet.getString("user_student_name");
            String surname = resultSet.getString("user_student_surname");
            String lastname = resultSet.getString("user_student_lastname");
            String login_ = resultSet.getString("user_student_login");
            String password = resultSet.getString("user_student_password");
            String email = resultSet.getString("user_student_email");
            String telephone = resultSet.getString("user_student_telephone");
            UUID companyId_ = null;
            if (resultSet.getString("company_id") != null) {
                companyId_ = UUID.fromString(resultSet.getString("company_id"));
            }
            long profileImageOid = resultSet.getLong("user_student_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
    }

    @Override
    public List<UserStudent> getAllCompanyStudentsThatNotInCourse(UUID companyId, UUID courseId) {
        final String sql = String.format("SELECT * FROM User_Students " +
                "WHERE company_id = '%s' " +
                "AND user_student_id != ALL (" +
                "SELECT student_id FROM Students_Courses " +
                "WHERE course_id = '%s');", companyId, courseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_student_id"));
            String name = resultSet.getString("user_student_name");
            String surname = resultSet.getString("user_student_surname");
            String lastname = resultSet.getString("user_student_lastname");
            String login_ = resultSet.getString("user_student_login");
            String password = resultSet.getString("user_student_password");
            String email = resultSet.getString("user_student_email");
            String telephone = resultSet.getString("user_student_telephone");
            UUID companyId_ = null;
            if (resultSet.getString("company_id") != null) {
                companyId_ = UUID.fromString(resultSet.getString("company_id"));
            }
            long profileImageOid = resultSet.getLong("user_student_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
    }

    @Override
    public boolean updateCourseById(UUID courseId, Course course) {

        final String sql = String.format("UPDATE Courses SET " +
                "course_name = '%s', " +
                "course_description = '%s', " +
                "course_is_active = %s, " +
                "course_start_date = '%s', " +
                "course_end_date = %s, " +
                "course_price = '%s', " +
                "course_payout_num = '%d', " +
                "user_teacher_id = '%s', " +
                "company_id = '%s' " +
                "WHERE course_id = '%s';",
                course.getName(),
                course.getDescription(),
                course.isActive(),
                course.getStartDate(),
                course.getEndDate().isEmpty() ? null : "'" + course.getEndDate() + "'",
                course.getPriceString(),
                course.getPayoutNum(),
                course.getTeacherId(),
                course.getCompanyId(),
                course.getId());
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean addStudentToCourse(UUID studentId, UUID courseId) {
        final String sql = String.format("INSERT INTO Students_Courses " +
                "(student_course_id, student_id, course_id) " +
                "VALUES (uuid_generate_v4(), '%s', '%s');",
                studentId, courseId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean deleteStudentFromCourse(UUID studentId, UUID courseId) {
        final String sql = String.format("DELETE FROM Students_Courses " +
                "WHERE student_id = '%s' AND course_id = '%s';", studentId, courseId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public Optional<? extends UserDetails> selectUserByUsername(String username) {
        Optional<UserStudent> student = selectUserStudentByLogin(username);
        if (student.isPresent()) {
            return student;
        }
        Optional<UserTeacher> teacher = selectUserTeacherByLogin(username);
        if (teacher.isPresent()) {
            return teacher;
        }
        return selectManagementStaffByLogin(username);
    }

    @Override
    public Optional<UserStudent> selectUserStudentByLogin(String login) {
        final String sql = String.format("SELECT * FROM User_Students WHERE user_student_login = '%s';", login);
        List<UserStudent> userStudents = jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_student_id"));
            String name = resultSet.getString("user_student_name");
            String surname = resultSet.getString("user_student_surname");
            String lastname = resultSet.getString("user_student_lastname");
            String login_ = resultSet.getString("user_student_login");
            String password = resultSet.getString("user_student_password");
            String email = resultSet.getString("user_student_email");
            String telephone = resultSet.getString("user_student_telephone");
            UUID companyId = null;
            if (resultSet.getString("company_id") != null) {
                companyId = UUID.fromString(resultSet.getString("company_id"));
            }
            long profileImageOid = resultSet.getLong("user_student_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
        return userStudents.stream().findFirst();
    }

    @Override
    public Optional<ManagementStaff> selectManagementStaffByLogin(String login) {
        final String sql = String.format("SELECT * FROM Management_Staff WHERE management_staff_login = '%s'", login);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("management_staff_id"));
            String name = resultSet.getString("management_staff_name");
            String surname = resultSet.getString("management_staff_surname");
            String lastname = resultSet.getString("management_staff_lastname");
            String login_ = resultSet.getString("management_staff_login");
            String password = resultSet.getString("management_staff_password");
            String email = resultSet.getString("management_staff_email");
            String telephone = resultSet.getString("management_staff_telephone");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId = UUID.fromString(resultSet.getString("company_id"));
            int role = resultSet.getInt("role");
            boolean isAbleToDeleteManager = resultSet.getBoolean("is_able_to_delete_manager");
            boolean isAbleToDeleteTeacher = resultSet.getBoolean("is_able_to_delete_teacher");
            boolean isAbleToDeleteStudent = resultSet.getBoolean("is_able_to_delete_student");
            boolean isAbleToAddManager = resultSet.getBoolean("is_able_to_add_manager");
            boolean isAbleToAddTeacher = resultSet.getBoolean("is_able_to_add_teacher");
            boolean isAbleToAddStudent = resultSet.getBoolean("is_able_to_add_student");
            boolean isAbleToDeleteCourse = resultSet.getBoolean("is_able_to_delete_course");
            boolean isAbleToAddCourse = resultSet.getBoolean("is_able_to_add_course");
            boolean isAbleToDeleteSchedule = resultSet.getBoolean("is_able_to_delete_schedule");
            boolean isAbleToAddSchedule = resultSet.getBoolean("is_able_to_add_schedule");
            boolean isAbleToEditManagement = resultSet.getBoolean("is_able_to_edit_manager");
            boolean isAbleToEditTeacher = resultSet.getBoolean("is_able_to_edit_teacher");
            boolean isAbleToEditStudent = resultSet.getBoolean("is_able_to_edit_student");
            boolean isAbleToEditCourse = resultSet.getBoolean("is_able_to_edit_course");
            boolean isAbleToEditSchedule = resultSet.getBoolean("is_able_to_edit_schedule");
            return new ManagementStaff(id, name, surname, lastname, login_, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role, isAbleToDeleteManager, isAbleToDeleteTeacher, isAbleToDeleteStudent, isAbleToAddManager, isAbleToAddTeacher, isAbleToAddStudent, isAbleToDeleteCourse, isAbleToAddCourse, isAbleToDeleteSchedule, isAbleToAddSchedule, isAbleToEditManagement, isAbleToEditTeacher, isAbleToEditStudent, isAbleToEditCourse, isAbleToEditSchedule);
        })).stream().findFirst();
    }

    @Override
    public Optional<UserTeacher> selectUserTeacherByLogin(String login) {
        final String sql = String.format("SELECT * FROM User_Teachers WHERE user_teacher_login = '%s';", login);
        List<UserTeacher> teachers = jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_teacher_id"));
            String name = resultSet.getString("user_teacher_name");
            String surname = resultSet.getString("user_teacher_surname");
            String lastname = resultSet.getString("user_teacher_lastname");
            String login_ = resultSet.getString("user_teacher_login");
            String password = resultSet.getString("user_teacher_password");
            String email = resultSet.getString("user_teacher_email");
            String telephone = resultSet.getString("user_teacher_telephone");
            long profileImageOid = resultSet.getLong("user_teacher_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
        return teachers.stream().findFirst();
    }

    @Override
    public boolean saveUserStudent(String login, String name, String password, UUID companyId) {
        if (selectUserStudentByLogin(login).isPresent() ||
            selectManagementStaffByLogin(login).isPresent() ||
            selectUserTeacherByLogin(login).isPresent()) {
            return false;
        }
        password = passwordEncoder.encode(password);
        final String sql = String.format("INSERT INTO User_Students " +
                "(user_student_id, user_student_name, user_student_login, user_student_password, is_account_non_expired," +
                " is_account_non_locked, is_credentials_non_expired, is_enabled, company_id) " +
                "VALUES (uuid_generate_v4(), '%s', '%s', '%s', True, True, True, True, '%s');", name, login, password, companyId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public List<ManagementStaff> selectAllCompanyManagers(Company company) {
        final String sql = String.format("SELECT * FROM Management_Staff " +
                "WHERE company_id = '%s';", company.getId());
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("management_staff_id"));
            String name = resultSet.getString("management_staff_name");
            String surname = resultSet.getString("management_staff_surname");
            String lastname = resultSet.getString("management_staff_lastname");
            String login = resultSet.getString("management_staff_login");
            String password = resultSet.getString("management_staff_password");
            String email = resultSet.getString("management_staff_email");
            String telephone = resultSet.getString("management_staff_telephone");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId = UUID.fromString(resultSet.getString("company_id"));
            int role = resultSet.getInt("role");
            boolean isAbleToDeleteManager = resultSet.getBoolean("is_able_to_delete_manager");
            boolean isAbleToDeleteTeacher = resultSet.getBoolean("is_able_to_delete_teacher");
            boolean isAbleToDeleteStudent = resultSet.getBoolean("is_able_to_delete_student");
            boolean isAbleToAddManager = resultSet.getBoolean("is_able_to_add_manager");
            boolean isAbleToAddTeacher = resultSet.getBoolean("is_able_to_add_teacher");
            boolean isAbleToAddStudent = resultSet.getBoolean("is_able_to_add_student");
            boolean isAbleToDeleteCourse = resultSet.getBoolean("is_able_to_delete_course");
            boolean isAbleToAddCourse = resultSet.getBoolean("is_able_to_add_course");
            boolean isAbleToDeleteSchedule = resultSet.getBoolean("is_able_to_delete_schedule");
            boolean isAbleToAddSchedule = resultSet.getBoolean("is_able_to_add_schedule");
            boolean isAbleToEditManagement = resultSet.getBoolean("is_able_to_edit_manager");
            boolean isAbleToEditTeacher = resultSet.getBoolean("is_able_to_edit_teacher");
            boolean isAbleToEditStudent = resultSet.getBoolean("is_able_to_edit_student");
            boolean isAbleToEditCourse = resultSet.getBoolean("is_able_to_edit_course");
            boolean isAbleToEditSchedule = resultSet.getBoolean("is_able_to_edit_schedule");
            return new ManagementStaff(id, name, surname, lastname, login, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role, isAbleToDeleteManager, isAbleToDeleteTeacher, isAbleToDeleteStudent, isAbleToAddManager, isAbleToAddTeacher, isAbleToAddStudent, isAbleToDeleteCourse, isAbleToAddCourse, isAbleToDeleteSchedule, isAbleToAddSchedule, isAbleToEditManagement, isAbleToEditTeacher, isAbleToEditStudent, isAbleToEditCourse, isAbleToEditSchedule);
        }));
    }

    @Override
    public List<Course> getAllStudentCourses(UUID studentId) {
        final String sql = String.format("SELECT * FROM Courses " +
                "WHERE course_id = ANY (" +
                "SELECT course_id FROM Students_Courses " +
                "WHERE student_id = '%s');", studentId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("course_id"));
            String name = resultSet.getString("course_name");
            String description = resultSet.getString("course_description");
            boolean isActive = resultSet.getBoolean("course_is_active");
            String startDate = resultSet.getString("course_start_date");
            String endDate = resultSet.getString("course_end_date");
            double price = resultSet.getDouble("course_price");
            int payoutNum = resultSet.getInt("course_payout_num");
            UUID teacherId = UUID.fromString(resultSet.getString("user_teacher_id"));
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new Course(id, name, description, isActive, startDate, endDate, price, payoutNum, teacherId, companyId_);
        }));
    }

    @Override
    public List<UserTeacher> getAllStudentTeachers(UUID studentId) {
        final String sql = String.format("SELECT * FROM User_Teachers " +
                "WHERE user_teacher_id = ANY (" +
                "SELECT user_teacher_id FROM Courses, Students_Courses " +
                "WHERE Students_Courses.student_id = '%s' AND Students_Courses.course_id = Courses.course_id);", studentId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_teacher_id"));
            String name = resultSet.getString("user_teacher_name");
            String surname = resultSet.getString("user_teacher_surname");
            String lastname = resultSet.getString("user_teacher_lastname");
            String login_ = resultSet.getString("user_teacher_login");
            String password = resultSet.getString("user_teacher_password");
            String email = resultSet.getString("user_teacher_email");
            String telephone = resultSet.getString("user_teacher_telephone");
            long profileImageOid = resultSet.getLong("user_teacher_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId_, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
    }

    @Override
    public List<Course> getAllTeacherCourses(UUID teacherId) {
        final String sql = String.format("SELECT * FROM Courses " +
                "WHERE user_teacher_id = '%s';", teacherId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("course_id"));
            String name = resultSet.getString("course_name");
            String description = resultSet.getString("course_description");
            boolean isActive = resultSet.getBoolean("course_is_active");
            String startDate = resultSet.getString("course_start_date");
            String endDate = resultSet.getString("course_end_date");
            double price = resultSet.getDouble("course_price");
            int payoutNum = resultSet.getInt("course_payout_num");
            UUID teacherId_ = UUID.fromString(resultSet.getString("user_teacher_id"));
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new Course(id, name, description, isActive, startDate, endDate, price, payoutNum, teacherId_, companyId_);
        }));
    }

    @Override
    public List<UserStudent> getAllTeacherStudents(UUID teacherId) {
        final String sql = String.format("SELECT * FROM User_Students " +
                "WHERE user_student_id = ANY (" +
                "SELECT user_student_id FROM Students_Courses, Courses " +
                "WHERE Courses.user_teacher_id = '%s' AND Courses.course_id = Students_Courses.course_id);", teacherId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("user_student_id"));
            String name = resultSet.getString("user_student_name");
            String surname = resultSet.getString("user_student_surname");
            String lastname = resultSet.getString("user_student_lastname");
            String login_ = resultSet.getString("user_student_login");
            String password = resultSet.getString("user_student_password");
            String email = resultSet.getString("user_student_email");
            String telephone = resultSet.getString("user_student_telephone");
            UUID companyId_ = null;
            if (resultSet.getString("company_id") != null) {
                companyId_ = UUID.fromString(resultSet.getString("company_id"));
            }
            long profileImageOid = resultSet.getLong("user_student_profile_image");
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, profileImageOid, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
    }

    @Override
    public List<Schedule> getAllCourseSchedule(UUID courseId) {
        final String sql = String.format("SELECT * FROM Schedule " +
                "WHERE course_id = '%s' " +
                "ORDER BY schedule_time_start ASC;", courseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("schedule_id"));
            String startTime = resultSet.getString("schedule_time_start");
            String endTime = resultSet.getString("schedule_time_end");
            String weekDay = resultSet.getString("schedule_week_day");
            UUID courseId_ = UUID.fromString(resultSet.getString("course_id"));
            return new Schedule(id, startTime, endTime, weekDay, courseId_);
        }));
    }

    @Override
    public List<Schedule> getAllTeacherSchedule(UUID teacherId) {
        final String sql = String.format("SELECT * FROM Schedule " +
                "WHERE course_id = ANY (" +
                "SELECT course_id FROM Courses " +
                "WHERE user_teacher_id = '%s') " +
                "ORDER BY schedule_time_start ASC;", teacherId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("schedule_id"));
            String startTime = resultSet.getString("schedule_time_start");
            String endTime = resultSet.getString("schedule_time_end");
            String weekDay = resultSet.getString("schedule_week_day");
            UUID courseId_ = UUID.fromString(resultSet.getString("course_id"));
            return new Schedule(id, startTime, endTime, weekDay, courseId_);
        }));
    }

    @Override
    public List<Schedule> getAllStudentSchedule(UUID studentId) {
        final String sql = String.format("SELECT * FROM Schedule " +
                "WHERE course_id = ANY (" +
                "SELECT course_id FROM Students_Courses " +
                "WHERE student_id = '%s') " +
                "ORDER BY schedule_time_start ASC;", studentId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("schedule_id"));
            String startTime = resultSet.getString("schedule_time_start");
            String endTime = resultSet.getString("schedule_time_end");
            String weekDay = resultSet.getString("schedule_week_day");
            UUID courseId_ = UUID.fromString(resultSet.getString("course_id"));
            return new Schedule(id, startTime, endTime, weekDay, courseId_);
        }));
    }

    @Override
    public boolean addScheduleToCourse(Schedule schedule, UUID courseId) {
        for (Schedule s : getAllCourseSchedule(courseId)) {
            if (s.getWeekDay().equals(schedule.getWeekDay()) && timeIntersects(s.getStartTime(), s.getEndTime(), schedule.getStartTime(), schedule.getEndTime())) {
                return false;
            }
        }
        final String sql = String.format("INSERT INTO Schedule " +
                "(schedule_id, schedule_time_start, schedule_time_end, schedule_week_day, course_id) " +
                "VALUES ('%s', '%s', '%s', '%s', '%s');",
                schedule.getId(), schedule.getStartTime(), schedule.getEndTime(), schedule.getWeekDay(), courseId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean deleteScheduleFromCourse(UUID scheduleId) {
        final String sql = String.format("DELETE FROM Schedule " +
                "WHERE schedule_id = '%s';", scheduleId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public Map<Integer, List<Schedule>> getMappedCourseSchedule(UUID courseId) {
        return createMappedSchedule(getAllCourseSchedule(courseId));
    }

    @Override
    public Map<Integer, List<Schedule>> getMappedTeacherSchedule(UUID teacherId) {
        return createMappedSchedule(getAllTeacherSchedule(teacherId));
    }

    @Override
    public Map<Integer, List<Schedule>> getMappedStudentSchedule(UUID studentId) {
        return createMappedSchedule(getAllStudentSchedule(studentId));
    }

    @Override
    public List<Attendance> getAllCourseAttendances(UUID courseId) {
        final String sql = String.format("SELECT * FROM Attendance " +
                "WHERE student_course_id = ANY (" +
                "SELECT student_course_id FROM Students_Courses " +
                "WHERE course_id = '%s') " +
                "ORDER BY attendance_date DESC;", courseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("attendance_id"));
            Optional<StudentCourse> studentCourseOptional = getStudentCourseById(UUID.fromString(resultSet.getString("student_course_id")));
            StudentCourse studentCourse = null;
            if (studentCourseOptional.isPresent()) {
                studentCourse = studentCourseOptional.get();
            }
            String date = resultSet.getString("attendance_date");
            int attendanceType = resultSet.getInt("attendance_type");
            return new Attendance(id, studentCourse, date, attendanceType);
        }));
    }

    @Override
    public Optional<StudentCourse> getStudentCourseById(UUID studentCourseId) {
        final String sql = String.format("SELECT * FROM Students_Courses " +
                "WHERE student_course_id = '%s';", studentCourseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("student_course_id"));
            Optional<UserStudent> studentOptional = getStudentById(UUID.fromString(resultSet.getString("student_id")));
            UserStudent student = null;
            if (studentOptional.isPresent()) {
                student = studentOptional.get();
            }
            Optional<Course> courseOptional = getCourseById(UUID.fromString(resultSet.getString("course_id")));
            Course course = null;
            if (courseOptional.isPresent()) {
                course = courseOptional.get();
            }
            double reviewScore = resultSet.getDouble("student_course_review_score");
            return new StudentCourse(id, student, course, reviewScore);
        })).stream().findFirst();
    }

    @Override
    public boolean addAttendanceToCourse(UUID courseId, String date) {
        final String sql = "INSERT INTO Attendance " +
                "(attendance_id, student_course_id, attendance_date) " +
                "VALUES (uuid_generate_v4(), '%s', '%s');";
        List<StudentCourse> studentCourseList = getAllStudentCourseOfCourse(courseId);
        for (StudentCourse studentCourse : studentCourseList) {
            jdbcTemplate.execute(String.format(sql, studentCourse.getId(), date));
        }
        return true;
    }

    @Override
    public List<StudentCourse> getAllStudentCourseOfCourse(UUID courseId) {
        final String sql = String.format("SELECT * FROM Students_Courses " +
                "WHERE course_id = '%s';", courseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("student_course_id"));
            Optional<UserStudent> studentOptional = getStudentById(UUID.fromString(resultSet.getString("student_id")));
            UserStudent student = null;
            if (studentOptional.isPresent()) {
                student = studentOptional.get();
            }
            Optional<Course> courseOptional = getCourseById(UUID.fromString(resultSet.getString("course_id")));
            Course course = null;
            if (courseOptional.isPresent()) {
                course = courseOptional.get();
            }
            double reviewScore = resultSet.getDouble("student_course_review_score");
            return new StudentCourse(id, student, course, reviewScore);
        }));
    }

    @Override
    public List<Attendance> getAllCourseAttendancesForSpecificDate(UUID courseId, String date) {
        final String sql = String.format("SELECT * FROM Attendance " +
                "WHERE attendance_date = '%s' AND student_course_id = ANY (" +
                "SELECT student_course_id FROM Students_Courses " +
                "WHERE course_id = '%s');", date, courseId);
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("attendance_id"));
            Optional<StudentCourse> studentCourseOptional = getStudentCourseById(UUID.fromString(resultSet.getString("student_course_id")));
            StudentCourse studentCourse = null;
            if (studentCourseOptional.isPresent()) {
                studentCourse = studentCourseOptional.get();
            }
            String date_ = resultSet.getString("attendance_date");
            int attendanceType = resultSet.getInt("attendance_type");
            return new Attendance(id, studentCourse, date_, attendanceType);
        }));
    }

    @Override
    public boolean updateAttendances(Map<String, String> params) {
        final String sql = "UPDATE Attendance SET " +
                "attendance_type = %d " +
                "WHERE attendance_id = '%s';";
        Map<UUID, Integer> attendanceMap = createAttendanceMap(params);
        for (UUID id : attendanceMap.keySet()) {
            jdbcTemplate.execute(String.format(sql, attendanceMap.get(id), id));
        }
        return true;
    }

    @Override
    public boolean updateManagementStaffById(UUID managerId, ManagementStaff managementStaff) {
        if (selectUserTeacherByLogin(managementStaff.getLogin()).isPresent() ||
            selectUserStudentByLogin(managementStaff.getLogin()).isPresent()) {
            return false;
        }
        Optional<ManagementStaff> check = selectManagementStaffByLogin(managementStaff.getLogin());
        if (check.isPresent() && !check.get().getId().equals(managementStaff.getId())) {
            return false;
        }
        final String sql = String.format("UPDATE Management_Staff SET " +
                "management_staff_name = '%s', " +
                "management_staff_surname = '%s', " +
                "management_staff_lastname = '%s', " +
                "management_staff_login = '%s', " +
                "management_staff_password = '%s', " +
                "management_staff_email = '%s', " +
                "management_staff_telephone = '%s', " +
                "is_account_non_expired = %s, " +
                "is_account_non_locked = %s, " +
                "is_credentials_non_expired = %s, " +
                "is_enabled = %s, " +
                "company_id = '%s', " +
                "role = %d, " +
                "is_able_to_delete_manager = %s, " +
                "is_able_to_delete_teacher = %s, " +
                "is_able_to_delete_student = %s, " +
                "is_able_to_add_manager = %s, " +
                "is_able_to_add_teacher = %s, " +
                "is_able_to_add_student = %s, " +
                "is_able_to_delete_course = %s, " +
                "is_able_to_add_course = %s " +
                "WHERE management_staff_id = '%s';",
                managementStaff.getName(),
                managementStaff.getSurname(),
                managementStaff.getLastname(),
                managementStaff.getUsername(),
                passwordEncoder.encode(managementStaff.getPassword()),
                managementStaff.getEmail(),
                managementStaff.getTelephone(),
                managementStaff.isAccountNonExpired(),
                managementStaff.isAccountNonLocked(),
                managementStaff.isCredentialsNonExpired(),
                managementStaff.isEnabled(),
                managementStaff.getCompanyId(),
                managementStaff.getRole(),
                managementStaff.isAbleToDeleteManager(),
                managementStaff.isAbleToDeleteTeacher(),
                managementStaff.isAbleToDeleteStudent(),
                managementStaff.isAbleToAddManager(),
                managementStaff.isAbleToAddTeacher(),
                managementStaff.isAbleToAddStudent(),
                managementStaff.isAbleToDeleteCourse(),
                managementStaff.isAbleToAddCourse(),
                managerId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean updateUserTeacherById(UUID teacherId, UserTeacher userTeacher) {
        if (selectManagementStaffByLogin(userTeacher.getLogin()).isPresent() ||
            selectUserStudentByLogin(userTeacher.getLogin()).isPresent()) {
            return false;
        }
        Optional<UserTeacher> check = selectUserTeacherByLogin(userTeacher.getLogin());
        if (check.isPresent() && !check.get().getId().equals(userTeacher.getId())) {
            return false;
        }
        final String sql = String.format("UPDATE User_Teachers SET " +
                        "user_teacher_name = '%s', " +
                        "user_teacher_surname = '%s', " +
                        "user_teacher_lastname = '%s', " +
                        "user_teacher_login = '%s', " +
                        "user_teacher_password = '%s', " +
                        "user_teacher_email = '%s', " +
                        "user_teacher_telephone = '%s', " +
                        "is_account_non_expired = %s, " +
                        "is_account_non_locked = %s, " +
                        "is_credentials_non_expired = %s, " +
                        "is_enabled = %s, " +
                        "company_id = '%s' " +
                        "WHERE user_teacher_id = '%s';",
                userTeacher.getName(),
                userTeacher.getSurname(),
                userTeacher.getLastname(),
                userTeacher.getUsername(),
                passwordEncoder.encode(userTeacher.getPassword()),
                userTeacher.getEmail(),
                userTeacher.getTelephone(),
                userTeacher.isAccountNonExpired(),
                userTeacher.isAccountNonLocked(),
                userTeacher.isCredentialsNonExpired(),
                userTeacher.isEnabled(),
                userTeacher.getCompanyId(),
                teacherId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean updateUserStudentById(UUID studentId, UserStudent userStudent) {
        if (selectManagementStaffByLogin(userStudent.getLogin()).isPresent() ||
                selectUserTeacherByLogin(userStudent.getLogin()).isPresent()) {
            return false;
        }
        Optional<UserStudent> check = selectUserStudentByLogin(userStudent.getLogin());
        if (check.isPresent() && !check.get().getId().equals(userStudent.getId())) {
            return false;
        }
        final String sql = String.format("UPDATE User_Students SET " +
                        "user_student_name = '%s', " +
                        "user_student_surname = '%s', " +
                        "user_student_lastname = '%s', " +
                        "user_student_login = '%s', " +
                        "user_student_password = '%s', " +
                        "user_student_email = '%s', " +
                        "user_student_telephone = '%s', " +
                        "is_account_non_expired = %s, " +
                        "is_account_non_locked = %s, " +
                        "is_credentials_non_expired = %s, " +
                        "is_enabled = %s, " +
                        "company_id = '%s' " +
                        "WHERE user_student_id = '%s';",
                userStudent.getName(),
                userStudent.getSurname(),
                userStudent.getLastname(),
                userStudent.getUsername(),
                passwordEncoder.encode(userStudent.getPassword()),
                userStudent.getEmail(),
                userStudent.getTelephone(),
                userStudent.isAccountNonExpired(),
                userStudent.isAccountNonLocked(),
                userStudent.isCredentialsNonExpired(),
                userStudent.isEnabled(),
                userStudent.getCompanyId(),
                studentId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean updateManagementStaffWithoutPasswordById(UUID managerId, ManagementStaff managementStaff) {
        if (selectUserTeacherByLogin(managementStaff.getLogin()).isPresent() ||
                selectUserStudentByLogin(managementStaff.getLogin()).isPresent()) {
            return false;
        }
        Optional<ManagementStaff> check = selectManagementStaffByLogin(managementStaff.getLogin());
        if (check.isPresent() && !check.get().getId().equals(managementStaff.getId())) {
            return false;
        }
        final String sql = String.format("UPDATE Management_Staff SET " +
                        "management_staff_name = '%s', " +
                        "management_staff_surname = '%s', " +
                        "management_staff_lastname = '%s', " +
                        "management_staff_login = '%s', " +
                        "management_staff_email = '%s', " +
                        "management_staff_telephone = '%s', " +
                        "is_account_non_expired = %s, " +
                        "is_account_non_locked = %s, " +
                        "is_credentials_non_expired = %s, " +
                        "is_enabled = %s, " +
                        "company_id = '%s', " +
                        "role = %d, " +
                        "is_able_to_delete_manager = %s, " +
                        "is_able_to_delete_teacher = %s, " +
                        "is_able_to_delete_student = %s, " +
                        "is_able_to_add_manager = %s, " +
                        "is_able_to_add_teacher = %s, " +
                        "is_able_to_add_student = %s, " +
                        "is_able_to_delete_course = %s, " +
                        "is_able_to_add_course = %s " +
                        "WHERE management_staff_id = '%s';",
                managementStaff.getName(),
                managementStaff.getSurname(),
                managementStaff.getLastname(),
                managementStaff.getUsername(),
                managementStaff.getEmail(),
                managementStaff.getTelephone(),
                managementStaff.isAccountNonExpired(),
                managementStaff.isAccountNonLocked(),
                managementStaff.isCredentialsNonExpired(),
                managementStaff.isEnabled(),
                managementStaff.getCompanyId(),
                managementStaff.getRole(),
                managementStaff.isAbleToDeleteManager(),
                managementStaff.isAbleToDeleteTeacher(),
                managementStaff.isAbleToDeleteStudent(),
                managementStaff.isAbleToAddManager(),
                managementStaff.isAbleToAddTeacher(),
                managementStaff.isAbleToAddStudent(),
                managementStaff.isAbleToDeleteCourse(),
                managementStaff.isAbleToAddCourse(),
                managerId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean updateUserTeacherWithoutPasswordById(UUID teacherId, UserTeacher userTeacher) {
        if (selectManagementStaffByLogin(userTeacher.getLogin()).isPresent() ||
                selectUserStudentByLogin(userTeacher.getLogin()).isPresent()) {
            return false;
        }
        Optional<UserTeacher> check = selectUserTeacherByLogin(userTeacher.getLogin());
        if (check.isPresent() && !check.get().getId().equals(userTeacher.getId())) {
            return false;
        }
        final String sql = String.format("UPDATE User_Teachers SET " +
                        "user_teacher_name = '%s', " +
                        "user_teacher_surname = '%s', " +
                        "user_teacher_lastname = '%s', " +
                        "user_teacher_login = '%s', " +
                        "user_teacher_email = '%s', " +
                        "user_teacher_telephone = '%s', " +
                        "is_account_non_expired = %s, " +
                        "is_account_non_locked = %s, " +
                        "is_credentials_non_expired = %s, " +
                        "is_enabled = %s, " +
                        "company_id = '%s' " +
                        "WHERE user_teacher_id = '%s';",
                userTeacher.getName(),
                userTeacher.getSurname(),
                userTeacher.getLastname(),
                userTeacher.getUsername(),
                userTeacher.getEmail(),
                userTeacher.getTelephone(),
                userTeacher.isAccountNonExpired(),
                userTeacher.isAccountNonLocked(),
                userTeacher.isCredentialsNonExpired(),
                userTeacher.isEnabled(),
                userTeacher.getCompanyId(),
                teacherId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public boolean updateUserStudentWithoutPasswordById(UUID studentId, UserStudent userStudent) {
        if (selectManagementStaffByLogin(userStudent.getLogin()).isPresent() ||
                selectUserTeacherByLogin(userStudent.getLogin()).isPresent()) {
            return false;
        }
        Optional<UserStudent> check = selectUserStudentByLogin(userStudent.getLogin());
        if (check.isPresent() && !check.get().getId().equals(userStudent.getId())) {
            return false;
        }
        final String sql = String.format("UPDATE User_Students SET " +
                        "user_student_name = '%s', " +
                        "user_student_surname = '%s', " +
                        "user_student_lastname = '%s', " +
                        "user_student_login = '%s', " +
                        "user_student_email = '%s', " +
                        "user_student_telephone = '%s', " +
                        "is_account_non_expired = %s, " +
                        "is_account_non_locked = %s, " +
                        "is_credentials_non_expired = %s, " +
                        "is_enabled = %s, " +
                        "company_id = '%s' " +
                        "WHERE user_student_id = '%s';",
                userStudent.getName(),
                userStudent.getSurname(),
                userStudent.getLastname(),
                userStudent.getUsername(),
                userStudent.getEmail(),
                userStudent.getTelephone(),
                userStudent.isAccountNonExpired(),
                userStudent.isAccountNonLocked(),
                userStudent.isCredentialsNonExpired(),
                userStudent.isEnabled(),
                userStudent.getCompanyId(),
                studentId);
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public Optional<Admin> selectAdminByLogin(String login) {
        final String sql = String.format("SELECT * FROM Admins WHERE admin_login = '%s'", login);
        List<Admin> admins = jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("admin_id"));
            String login_ = resultSet.getString("admin_login");
            String password = resultSet.getString("admin_password");
            return new Admin(id, login_, password);
        }));
        return admins.stream().findFirst();
    }

    @Override
    public boolean changeStudentProfilePicture(UUID studentId, Resource image) {
        try {
            Connection connection = postgresDataSource.getHikariDataSource().getConnection();
            connection.setAutoCommit(false);
            PGConnection pgConnection;
            if (connection.isWrapperFor(PGConnection.class)) {
                pgConnection = connection.unwrap(PGConnection.class);
            } else {
                return false;
            }
            LargeObjectManager largeObjectManager = pgConnection.getLargeObjectAPI();
            long oid = largeObjectManager.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
            LargeObject largeObject = largeObjectManager.open(oid, LargeObjectManager.WRITE);
            byte[] buf = image.getInputStream().readAllBytes();
            //System.out.println(buf.length);
            largeObject.write(buf, 0, buf.length);
            largeObject.close();
            final String sql = String.format("UPDATE User_Students SET " +
                    "user_student_profile_image = %d " +
                    "WHERE user_student_id = '%s';", oid, studentId);
            jdbcTemplate.execute(sql);
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Resource getStudentProfilePicture(UserStudent student) {
        try {
            Connection connection = postgresDataSource.getHikariDataSource().getConnection();
            connection.setAutoCommit(false);
            PGConnection pgConnection;

            if (connection.isWrapperFor(PGConnection.class)) {
                pgConnection = connection.unwrap(PGConnection.class);
            } else {
                return null;
            }

            LargeObjectManager largeObjectManager = pgConnection.getLargeObjectAPI();
            System.out.println(student.getProfileImageOid());
            if (student.getProfileImageOid() == 0) {
                return null;
            }
            LargeObject largeObject = largeObjectManager.open(student.getProfileImageOid(), LargeObjectManager.READ);
//            byte[] buf = new byte[largeObject.size()];
//            largeObject.read(buf, 0, largeObject.size());
            ByteArrayResource byteArrayResource = new ByteArrayResource(largeObject.getInputStream().readAllBytes());
            largeObject.close();
            connection.commit();
            connection.setAutoCommit(true);
            return byteArrayResource;
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean changeTeacherProfilePicture(UUID teacherId, Resource image) {
        try {
            Connection connection = postgresDataSource.getHikariDataSource().getConnection();
            connection.setAutoCommit(false);
            PGConnection pgConnection;
            if (connection.isWrapperFor(PGConnection.class)) {
                pgConnection = connection.unwrap(PGConnection.class);
            } else {
                return false;
            }
            LargeObjectManager largeObjectManager = pgConnection.getLargeObjectAPI();
            long oid = largeObjectManager.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
            LargeObject largeObject = largeObjectManager.open(oid, LargeObjectManager.WRITE);
            byte[] buf = image.getInputStream().readAllBytes();
            //System.out.println(buf.length);
            largeObject.write(buf, 0, buf.length);
            largeObject.close();
            final String sql = String.format("UPDATE User_Teachers SET " +
                    "user_teacher_profile_image = %d " +
                    "WHERE user_teacher_id = '%s';", oid, teacherId);
            jdbcTemplate.execute(sql);
            connection.commit();
            connection.setAutoCommit(true);
            return true;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Resource getTeacherProfilePicture(UserTeacher teacher) {
        try {
            Connection connection = postgresDataSource.getHikariDataSource().getConnection();
            connection.setAutoCommit(false);
            PGConnection pgConnection;

            if (connection.isWrapperFor(PGConnection.class)) {
                pgConnection = connection.unwrap(PGConnection.class);
            } else {
                return null;
            }

            LargeObjectManager largeObjectManager = pgConnection.getLargeObjectAPI();
            System.out.println(teacher.getProfileImageOid());
            if (teacher.getProfileImageOid() == 0) {
                return null;
            }
            LargeObject largeObject = largeObjectManager.open(teacher.getProfileImageOid(), LargeObjectManager.READ);
//            byte[] buf = new byte[largeObject.size()];
//            largeObject.read(buf, 0, largeObject.size());
            ByteArrayResource byteArrayResource = new ByteArrayResource(largeObject.getInputStream().readAllBytes());
            largeObject.close();
            connection.commit();
            connection.setAutoCommit(true);
            return byteArrayResource;
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    private boolean timeIntersects(String startTime1, String endTime1, String startTime2, String endTime2) {
        int st1 = timeToInt(startTime1);
        int et1 = timeToInt(endTime1);
        int st2 = timeToInt(startTime2);
        int et2 = timeToInt(endTime2);
        return (st1 <= st2 && st2 <= et1) || (st1 <= et2 && et2 <= et1) ||
                (st2 <= st1 && st1 <= et2) || (st2 <= et1 && et1 <= et2);
    }

    private int timeToInt(String time) {
        String[] s = time.split(":");
        return Integer.parseInt(s[0]) * 60 + Integer.parseInt(s[1]);
    }

    private Map<Integer, List<Schedule>> createMappedSchedule(List<Schedule> scheduleList) {
        Map<Integer, List<Schedule>> res = new HashMap<>();
        for (int i = 1; i <= 7; ++i) {
            res.put(i, new ArrayList<>());
        }
        for (Schedule schedule : scheduleList) {
            switch (schedule.getWeekDay()) {
                case "Monday": res.get(1).add(schedule); break;
                case "Tuesday": res.get(2).add(schedule); break;
                case "Wednesday": res.get(3).add(schedule); break;
                case "Thursday": res.get(4).add(schedule); break;
                case "Friday": res.get(5).add(schedule); break;
                case "Saturday": res.get(6).add(schedule); break;
                case "Sunday": res.get(7).add(schedule); break;
            }
        }
        return res;
    }

    private Map<UUID, Integer> createAttendanceMap(Map<String, String> params) {
        Map<UUID, Integer> attendanceMap = new HashMap<>();
        for (String param : params.keySet()) {
            if (param.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
                UUID attendanceId = UUID.fromString(param);
                int attendanceType = Integer.parseInt(params.get(param));
                attendanceMap.put(attendanceId, attendanceType);
            }
        }
        return attendanceMap;
    }
}
