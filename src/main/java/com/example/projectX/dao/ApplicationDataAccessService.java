package com.example.projectX.dao;

import com.example.projectX.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
        if (selectManagementStaffByLogin(managerLogin).isPresent()) {
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
    public Optional<User> selectUserByUsername(String username) {
        final String sql = String.format("SELECT * FROM Users WHERE username = '%s';", username);
        List<User> users = jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID uuid = UUID.fromString(resultSet.getString("id"));
            String username_ = resultSet.getString("username");
            String password = passwordEncoder.encode(resultSet.getString("password"));

            return new User(uuid, username_, password, null, true, true, true, true);
        }));
        return users.stream().findFirst();
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
            return new ManagementStaff(id, name, surname, lastname, login_, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role);
        })).stream().findFirst();
    }

    @Override
    public boolean saveUserStudent(String login, String name, String password, UUID companyId) {
        if (selectUserStudentByLogin(login).isPresent()) {
            return false;
        }
        password = passwordEncoder.encode(password);
        final String sql = String.format("INSERT INTO User_Students " +
                "(user_student_id, user_student_name, user_student_login, user_student_password, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled) " +
                "VALUES (uuid_generate_v4(), '%s', '%s', '%s', True, True, True, True);", name, login, password);
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
            return new ManagementStaff(id, name, surname, lastname, login, password, email, telephone, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled, companyId, role);
        }));
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
}
