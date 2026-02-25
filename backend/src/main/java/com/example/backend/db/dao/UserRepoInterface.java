package com.example.backend.db.dao;

import com.example.backend.models.User;

public interface UserRepoInterface {

    public User getUser(User user);

    public int postUser(User user);
}
