package com.example.backend.db.dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class PictureRepo implements PictureRepoInterface{

    private String picturesPath = "backend/src/main/resources/static/pictures/";

    @Override
    public int postPicture(MultipartFile file, String id) {

        try {
            if (file.isEmpty()) {
                return -1;
            }

            File dir = new File(picturesPath);
            if (!dir.exists()) dir.mkdirs();

            Path filePath = Paths.get(picturesPath, id + ".png");
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return 1;

        } catch (IOException e) {
            return -2;
        }
    }

    @Override
    public ResponseEntity<Resource> getPicture(String id) {
        try {
            Path path = Paths.get(picturesPath + id + ".png");
            Resource resource = new UrlResource(path.toUri());

            return ResponseEntity.ok().body(resource);

        } catch(IOException e) {
            return null;
        }
    }
    
}
