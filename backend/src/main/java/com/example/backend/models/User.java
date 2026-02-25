package com.example.backend.models;

public class User {
    
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String number;
    private String email;
    private String firmName;
    private String firmAdress;
    private String companyRegistrationNumber;
    private String taxIdentificationNumber;
    private String type;

    public User(String username, String password, String name, String lastName, String number, String email, 
        String firmName, String firmAdress, String companyRegistrationNumber, String taxIdentificationNumber, String type) {
        this.username = username;
        this.password = password;
        this.firstname = name;
        this.lastname = lastName;
        this.number = number;
        this.email = email;
        this.firmName = firmName;
        this.firmAdress = firmAdress;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.type = type;
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }
    public void setFirmAdress(String firmAdress) {
        this.firmAdress = firmAdress;
    }
    public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
        this.companyRegistrationNumber = companyRegistrationNumber;
    }
    public void setTaxIdentificationNumber(String taxIdentificationNumber) {
        this.taxIdentificationNumber = taxIdentificationNumber;
    }
    public String getFirmName() {
        return firmName;
    }
    public String getFirmAdress() {
        return firmAdress;
    }
    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }
    public String getTaxIdentificationNumber() {
        return taxIdentificationNumber;
    }
}
