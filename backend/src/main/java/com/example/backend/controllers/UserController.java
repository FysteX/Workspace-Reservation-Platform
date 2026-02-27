package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.UserRepo;
import com.example.backend.models.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:4200/")
public class UserController {
    
    @PostMapping("getUser")
    public User getUser(@RequestBody User user) {
       return new UserRepo().getUser(user);
    }

    @PostMapping("postUser")
    public int postUser(@RequestBody User user) {
        return new UserRepo().postUser(user);
    }

    @GetMapping("getPendingUsers")
    public List<User> getPendingUsers() {
        return new UserRepo().getPendingUsers();
    }    
}
