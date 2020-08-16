package com.example.projectX.models;

import java.util.UUID;

public class Schedule {

    private final UUID id;
    private final String startTime;
    private final String endTime;
    private final String weekDay;
    private final UUID courseId;

    public Schedule(UUID id, String startTime, String endTime, String weekDay, UUID courseId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.weekDay = weekDay;
        this.courseId = courseId;
    }

    public UUID getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public UUID getCourseId() {
        return courseId;
    }
}
