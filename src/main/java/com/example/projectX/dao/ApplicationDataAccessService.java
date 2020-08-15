package com.example.projectX.dao;

import com.example.projectX.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class ApplicationDataAccessService implements CompanyDao, UserDao, AdminDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationDataAccessService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
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
                "VALUES ('%s', '%s', '%s', '%s', '%s', %f, %d, '%s', '%s');",
                course.getId(), course.getName(), course.getDescription(), course.isActive(), course.getStarDate(), course.getPrice(), course.getPayoutNum(), course.getTeacherId(), companyId);
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
            return new ManagementStaff(id_, name, surname, lastname, login_, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role, isAbleToDeleteManager, isAbleToDeleteTeacher, isAbleToDeleteStudent, isAbleToAddManager, isAbleToAddTeacher, isAbleToAddStudent, isAbleToDeleteCourse, isAbleToAddCourse);
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId_, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);

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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
                "course_price = '%f', " +
                "course_payout_num = '%d', " +
                "user_teacher_id = '%s', " +
                "company_id = '%s' " +
                "WHERE course_id = '%s';",
                course.getName(),
                course.getDescription(),
                course.isActive(),
                course.getStarDate(),
                course.getEndDate().isEmpty() ? null : "'" + course.getEndDate() + "'",
                course.getPrice(),
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
            return new ManagementStaff(id, name, surname, lastname, login_, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role, isAbleToDeleteManager, isAbleToDeleteTeacher, isAbleToDeleteStudent, isAbleToAddManager, isAbleToAddTeacher, isAbleToAddStudent, isAbleToDeleteCourse, isAbleToAddCourse);
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
            return new ManagementStaff(id, name, surname, lastname, login, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role, isAbleToDeleteManager, isAbleToDeleteTeacher, isAbleToDeleteStudent, isAbleToAddManager, isAbleToAddTeacher, isAbleToAddStudent, isAbleToDeleteCourse, isAbleToAddCourse);
        }));
    }

    @Override
    public List<Course> getAllStudentCourses(UUID studentId) {
        final String sql = String.format("SELECT * FROM Courses " +
                "WHERE course_id = ANY (" +
                "SELECT course_id FROM Students_Courses " +
                "WHERE student_id = '%s');");
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            UUID companyId_ = UUID.fromString(resultSet.getString("company_id"));
            return new UserTeacher(id, name, surname, lastname, login_, password, email, telephone, companyId_, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
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
            boolean isAccountNonExpired = resultSet.getBoolean("is_account_non_expired");
            boolean isAccountNonLocked = resultSet.getBoolean("is_account_non_locked");
            boolean isCredentialsNonExpired = resultSet.getBoolean("is_credentials_non_expired");
            boolean isEnabled = resultSet.getBoolean("is_enabled");
            return new UserStudent(id, name, surname, lastname, login_, password, email, telephone, companyId_, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled);
        }));
    }

    @Override
    public List<Schedule> getAllCourseSchedule(UUID courseId) {
        final String sql = String.format("SELECT * FROM Schedule " +
                "WHERE course_id = '%s';", courseId);
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
}
