package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.User;

public interface UserRepoInterface {

    public User getUser(User user);

    public int postUser(User user);

    public int getNumOfManagersForCompany(String companyRegistrationNumber);

    public List<User> getPendingUsers();
}
