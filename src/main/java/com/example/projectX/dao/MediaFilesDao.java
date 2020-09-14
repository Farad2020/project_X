package com.example.projectX.dao;

import com.example.projectX.models.UserStudent;
import com.example.projectX.models.UserTeacher;
import org.springframework.core.io.Resource;

import java.util.UUID;

public interface MediaFilesDao {

    boolean changeStudentProfilePicture(UUID studentId, Resource image);

    Resource getStudentProfilePicture(UserStudent student);

    boolean changeTeacherProfilePicture(UUID teacherId, Resource image);

    Resource getTeacherProfilePicture(UserTeacher teacher);
}
