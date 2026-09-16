package com.example.backend.db.dao;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import org.springframework.web.multipart.MultipartFile;

public interface PictureRepoInterface {
    
    public int postPicture(MultipartFile file, String id);

    public ResponseEntity<Resource> getPicture(String id);

}
