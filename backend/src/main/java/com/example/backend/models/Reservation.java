package com.example.backend.models;

public class Reservation {
    private int idReservation;
    private String roomName;
    private String user;
    private String workspaceName;
    private String city;
    private String date;
    private String startTime;
    private String endTime;
    private boolean active;
    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public int getIdReservation() {
        return idReservation;
    }
    public String getUser() {
        return user;
    }
    public String getWorkspaceName() {
        return workspaceName;
    }
    public String getCity() {
        return city;
    }
    public String getDate() {
        return date;
    }
    public boolean isActive() {
        return active;
    }
    
    public String getRoomName() {
        return roomName;
    }
    
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public Reservation(int idReservation, String roomName, String user, String workspaceName, String city, String date,
            String startTime, String endTime, boolean active) {
        this.idReservation = idReservation;
        this.roomName = roomName;
        this.user = user;
        this.workspaceName = workspaceName;
        this.city = city;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = active;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
