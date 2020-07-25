package com.example.projectX.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Company {
    private UUID id;
    private String name;
    private String email;
    private String telephone;

    public Company(@JsonProperty("id") UUID id,
                   @JsonProperty("name") String name,
                   @JsonProperty("email") String email,
                   @JsonProperty("telephone") String telephone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }
}
