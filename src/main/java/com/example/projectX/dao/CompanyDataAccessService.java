package com.example.projectX.dao;

import com.example.projectX.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("postgres")
public class CompanyDataAccessService implements CompanyDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertCompany(UUID id, Company company) {
        return 0;
    }

    @Override
    public List<Company> getAllCompanies() {
        final String sql = "SELECT id, name, email, telephone FROM Companies";

        return jdbcTemplate.query(
                sql,
                ((resultSet, i) -> {
                    UUID id = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String telephone = resultSet.getString("telephone");
                    return new Company(id, name, email, telephone);
                })
        );
    }
}
