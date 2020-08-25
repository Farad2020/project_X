package com.example.projectX.dao;

import com.example.projectX.models.UserStudent;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MediaFilesDao {

    boolean changeStudentProfilePicture(UUID studentId, MultipartFile image);

    byte[] getStudentProfilePicture(UserStudent student);
}
