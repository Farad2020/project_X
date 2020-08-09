package com.example.projectX.dao;

import com.example.projectX.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("user_auth")
public class UserDataAccessService implements UserDao, AdminDao {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDataAccessService(PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
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
        return null;
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
