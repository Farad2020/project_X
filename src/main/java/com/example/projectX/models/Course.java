package com.example.projectX.models;

import java.util.Date;
import java.util.UUID;

public class Course {

    private final UUID id;
    private final String name;
    private final String description;
    private final boolean active;
    private final String startDate;
    private final String endDate;
    private final double price;
    private final int payoutNum;
    private final UUID teacherId;
    private final UUID companyId;

    public Course(UUID id, String name, String description, boolean active, String startDate, String endDate, double price, int payoutNum, UUID teacherId, UUID companyId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.startDate = startDate;
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
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public double getPrice() {
        return price;
    }

    public String getPriceString() {
        return ((Double) price).toString().replaceAll(",",".");
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
