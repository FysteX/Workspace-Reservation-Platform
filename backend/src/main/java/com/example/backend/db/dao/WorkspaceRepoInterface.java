package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Workspace;

public interface WorkspaceRepoInterface {
    
    List<Workspace> getWorkspacesForCities(String[] cities);

    Workspace getWorkspace(String name);

    int getNumOfWorkspaces();

    List<String> getCities();
}
