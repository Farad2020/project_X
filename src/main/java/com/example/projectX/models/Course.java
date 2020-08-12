package com.example.projectX.models;

import java.util.Date;
import java.util.UUID;

public class Course {

    private final UUID id;
    private final String name;
    private final boolean active;
    private final Date starDate;
    private final Date endDate;
    private final double price;
    private final int payoutNum;
    private final UserTeacher teacher;
    private final UUID companyId;

    public Course(UUID id, String name, boolean active, Date starDate, Date endDate, double price, int payoutNum, UserTeacher teacher, UUID companyId) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.starDate = starDate;
        this.endDate = endDate;
        this.price = price;
        this.payoutNum = payoutNum;
        this.teacher = teacher;
        this.companyId = companyId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Date getStarDate() {
        return starDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getPrice() {
        return price;
    }

    public int getPayoutNum() {
        return payoutNum;
    }

    public UserTeacher getTeacher() {
        return teacher;
    }

    public UUID getCompanyId() {
        return companyId;
    }
}
