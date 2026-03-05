package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.WorkspaceRepo;
import com.example.backend.models.User;
import com.example.backend.models.Workspace;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/workspaces")
@CrossOrigin(origins = "http://localhost:4200/")
public class WorkspaceController {
    
    @PostMapping("getWorkspace")
    public Workspace getWorkspace(@RequestBody String name) {
        return new WorkspaceRepo().getWorkspace(name);
    }

    @PostMapping("getWorkspacesForCities")
    public List<Workspace> getWorkspacesForCities(@RequestBody String[] cities) {
        return new WorkspaceRepo().getWorkspacesForCities(cities);
    }
    
    @GetMapping("getNumOfWorkspaces")
    public int getNumOfWorkspaces() {
        return new WorkspaceRepo().getNumOfWorkspaces();
    }

    @GetMapping("getCities")
    public List<String> getCities() {
        return new WorkspaceRepo().getCities();
    }

    @PostMapping("getWorkspacesForManager")
    public List<Workspace> getWorkspacesForManager(@RequestBody User user) {
        return new WorkspaceRepo().getWorkspacesForManager(user);
    }

    @PostMapping("postWorkspace")
    public int postWorkspace(@RequestBody Workspace workspace) {
        return new WorkspaceRepo().postWorkspace(workspace);
    }

    @PostMapping("updateWorkspace")
    public int updateWorkspace(@RequestBody Workspace workspace) {
        return new WorkspaceRepo().updateWorkspace(workspace);
    }
}
