package com.example.projectX.models;

import java.util.UUID;

public class StudentCourse {

    private final UUID id;
    private final UserStudent student;
    private final Course course;
    private final double reviewScore;

    public StudentCourse(UUID id, UserStudent student, Course course, double reviewScore) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.reviewScore = reviewScore;
    }

    public UUID getId() {
        return id;
    }

    public UserStudent getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getReviewScore() {
        return reviewScore;
    }
}
