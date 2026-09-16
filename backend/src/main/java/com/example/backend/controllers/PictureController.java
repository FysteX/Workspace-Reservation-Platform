package com.example.backend.controllers;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.db.dao.PictureRepo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/pictures")
@CrossOrigin(origins = "http://localhost:4200/")
public class PictureController {
    
    @PostMapping("postPicture")
    public int postPicture(@RequestParam("file") MultipartFile file, @RequestParam("id") String id) {
        return new PictureRepo().postPicture(file, id);
    }

    @PostMapping("getPicture")
    public ResponseEntity<Resource> getPicture(@RequestBody String id) {
        return new PictureRepo().getPicture(id);
    }

}
