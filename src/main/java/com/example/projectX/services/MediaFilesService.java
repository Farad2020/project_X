package com.example.projectX.services;

import com.example.projectX.dao.MediaFilesDao;
import com.example.projectX.models.UserStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class MediaFilesService {

    private final MediaFilesDao mediaFilesDao;

    @Autowired
    public MediaFilesService(@Qualifier("postgres") MediaFilesDao mediaFilesDao) {
        this.mediaFilesDao = mediaFilesDao;
    }

    public boolean changeStudentProfilePicture(UUID studentId, MultipartFile image) {
        return mediaFilesDao.changeStudentProfilePicture(studentId, image);
    }

    public byte[] getStudentProfilePicture(UserStudent student) {
        return mediaFilesDao.getStudentProfilePicture(student);
    }
}
