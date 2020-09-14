package com.example.projectX.models;

import java.util.UUID;

public class Attendance {

    private final UUID id;
    private final StudentCourse studentCourse;
    private final String date;
    private final int attendanceType;

    public Attendance(UUID id, StudentCourse studentCourse, String date, int attendanceType) {
        this.id = id;
        this.studentCourse = studentCourse;
        this.date = date;
        this.attendanceType = attendanceType;
    }

    public UUID getId() {
        return id;
    }

    public StudentCourse getStudentCourse() {
        return studentCourse;
    }

    public String getDate() {
        return date;
    }

    public int getAttendanceType() {
        return attendanceType;
    }

    public String getAttendanceTypeString() {
        switch (attendanceType) {
            case 0: return "Прибыл";
            case 1: return "Не прибыл";
            case 2: return "Отсутствовал по причине";
            default: return "";
        }
    }
}
