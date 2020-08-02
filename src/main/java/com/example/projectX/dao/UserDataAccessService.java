package com.example.projectX.dao;

import com.example.projectX.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("user_auth")
public class UserDataAccessService implements UserDao {

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
//            String name = resultSet.getString("name");
//            String surname = resultSet.getString("name");
//            String lastname = resultSet.getString("lastname");
            String username_ = resultSet.getString("username");
            String password = passwordEncoder.encode(resultSet.getString("password"));
//            String email = resultSet.getString("email");
//            String telephone = resultSet.getString("telephone");
//            UUID companyUuid = UUID.fromString(resultSet.getString("company_id"));
            return new User(uuid, username_, password, null, true, true, true, true);
        }));
        return users.stream().findFirst();
    }
}
