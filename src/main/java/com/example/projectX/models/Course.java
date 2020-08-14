package com.example.projectX.models;

import java.util.Date;
import java.util.UUID;

public class Course {

    private final UUID id;
    private final String name;
    private final String description;
    private final boolean active;
    private final String starDate;
    private final String endDate;
    private final double price;
    private final int payoutNum;
    private final UUID teacherId;
    private final UUID companyId;

    public Course(UUID id, String name, String description, boolean active, String starDate, String endDate, double price, int payoutNum, UUID teacherId, UUID companyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.starDate = starDate;
        this.endDate = endDate;
        this.price = price;
        this.payoutNum = payoutNum;
        this.teacherId = teacherId;
        this.companyId = companyId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public String getStarDate() {
        return starDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getPrice() {
        return price;
    }

    public int getPayoutNum() {
        return payoutNum;
    }

    public UUID getTeacherId() {
        return teacherId;
    }

    public UUID getCompanyId() {
        return companyId;
    }
}
