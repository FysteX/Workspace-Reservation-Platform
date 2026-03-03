package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Room;
import com.example.backend.models.Workspace;

public interface RoomRepoInterface {
    
    List<Room> getRoomsForWorkspace(Workspace[] workspaces);

    public int postRoom(Room room);
}
