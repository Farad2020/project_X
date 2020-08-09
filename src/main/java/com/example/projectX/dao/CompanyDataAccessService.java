package com.example.projectX.dao;

import com.example.projectX.models.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class CompanyDataAccessService implements CompanyDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean insertCompany(UUID id, Company company) {
        if (getCompanyByName(company.getName()).isPresent()) {
            return false;
        }

        final String sql = String.format("INSERT INTO Companies (id, name, email, telephone) VALUES ('%s', '%s', '%s', '%s')",
                id.toString(), company.getName(), company.getEmail(), company.getTelephone());
        jdbcTemplate.execute(sql);
        return true;
    }

    @Override
    public Optional<Company> getCompanyByName(String name) {
        return Optional.empty();
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
