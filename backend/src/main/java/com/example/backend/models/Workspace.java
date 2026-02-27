package com.example.backend.models;

public class Workspace {
    private String name;
    private String city;
    private int likes;
    private boolean activeStatus;
    private String adress;
    private String firmName;
    private String manager;
    private int tables;
    
    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setActiveStatus(boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getLikes() {
        return likes;
    }

    public boolean isActiveStatus() {
        return activeStatus;
    }

    public String getAdress() {
        return adress;
    }

    public String getFirmName() {
        return firmName;
    }

    public String getManager() {
        return manager;
    }

    public Workspace(String name, String city, int likes, boolean activeStatus, String adress, String firmName,
            String manager, int tables) {
        this.name = name;
        this.city = city;
        this.likes = likes;
        this.activeStatus = activeStatus;
        this.adress = adress;
        this.firmName = firmName;
        this.manager = manager;
        this.tables = tables;
    }

    public int getTables() {
        return tables;
    }

    public void setTables(int tables) {
        this.tables = tables;
    }
}
