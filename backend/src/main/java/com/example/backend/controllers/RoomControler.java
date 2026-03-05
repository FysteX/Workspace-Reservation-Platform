package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.RoomRepo;
import com.example.backend.models.Room;
import com.example.backend.models.Workspace;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "http://localhost:4200/")
public class RoomControler {
    
    @PostMapping("getRoomsForWorkspaces")
    public List<Room> postMethodName(@RequestBody Workspace[] workspaces) {
        return new RoomRepo().getRoomsForWorkspaces(workspaces);
    }
    
    @PostMapping("postRoom")
    public int postRoom(@RequestBody Room room ) {
        return new RoomRepo().postRoom(room);
    }

    @PostMapping("updateRoom")
    public int updateRoom(@RequestBody Room room ) {
        return new RoomRepo().updateRoom(room);
    }

}
