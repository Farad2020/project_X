package com.example.projectX.dao;

import com.example.projectX.models.UserStudent;
import org.springframework.core.io.Resource;

import java.util.UUID;

public interface MediaFilesDao {

    boolean changeStudentProfilePicture(UUID studentId, Resource image);

    Resource getStudentProfilePicture(UserStudent student);
}
