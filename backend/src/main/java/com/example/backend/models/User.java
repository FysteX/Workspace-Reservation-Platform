package com.example.backend.models;

public class User {
    
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String number;
    private String email;
    private String type;
    private boolean admin;

    public User(String username, String password, String name, String lastName, String number, String email, String type,boolean admin) {
        this.username = username;
        this.password = password;
        this.firstname = name;
        this.lastname = lastName;
        this.number = number;
        this.email = email;
        this.admin = admin;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getFirstname() {
        return firstname;
    }
    public String getLastname() {
        return lastname;
    }
    public String getNumber() {
        return number;
    }
    public String getEmail() {
        return email;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setFirstname(String name) {
        this.firstname = name;
    }
    public void setLastname(String lastName) {
        this.lastname = lastName;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public boolean isAdmin() {
        return admin;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
