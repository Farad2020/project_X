package com.example.projectX.models;

import java.util.UUID;

public class StudentCourse {

    private final UUID id;
    private final UUID studentId;
    private final UUID courseId;
    private final double reviewScore;

    public StudentCourse(UUID id, UUID studentId, UUID courseId, double reviewScore) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.reviewScore = reviewScore;
    }

    public UUID getId() {
        return id;
    }

    public UUID getStudentId() {
        return studentId;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public double getReviewScore() {
        return reviewScore;
    }
}
