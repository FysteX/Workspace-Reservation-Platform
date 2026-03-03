package com.example.backend.models;

public class Reservation {
    private int idReservation;
    private String roomName;
    private String user;
    private String workspaceName;
    private String city;
    private String startDate;
    private String endDate;
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
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
    public String getStartDate() {
        return startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public boolean isActive() {
        return active;
    }
    
    public String getRoomName() {
        return roomName;
    }
    public Reservation(int idReservation, String roomName, String user, String workspaceName, String city, String startDate,
            String endDate, boolean active) {
        this.idReservation = idReservation;
        this.roomName = roomName;
        this.user = user;
        this.workspaceName = workspaceName;
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
    }
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
