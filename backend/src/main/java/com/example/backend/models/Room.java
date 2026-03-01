package com.example.backend.models;

public class Room {
    private int idWorkspace;
    private String name;
    private String type;
    private int tables;
    private String description;
    public Room(int idWorkspace, String name, String type, int tables, String description) {
        this.idWorkspace = idWorkspace;
        this.name = name;
        this.type = type;
        this.tables = tables;
        this.description = description;
    }
    public int getIdWorkspace() {
        return idWorkspace;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
    public int getTables() {
        return tables;
    }
    public String getDescription() {
        return description;
    }
    public void setIdWorkspace(int idWorkspace) {
        this.idWorkspace = idWorkspace;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setTables(int tables) {
        this.tables = tables;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}
