package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Workspace;

public class WorkspaceRepo implements WorkspaceRepoInterface {

    @Override
    public List<Workspace> getWorkspacesForCities(String[] cities) {// mozda treba lista stringova
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from workspaces where city = ?");
        ) {
            List<Workspace> workspaces = new ArrayList<>();

            for(int i = 0 ; i < cities.length ; i++) {
                pstmt.setString(1, cities[i]);
                ResultSet rs = pstmt.executeQuery();
                while(rs.next()) {
                    workspaces.add(new Workspace(
                    rs.getInt("idWorkspace"),
                    rs.getString("name"),
                    rs.getString("city"),
                    rs.getInt("likes"),
                    rs.getBoolean("activeStatus"),
                    rs.getString("adress"), 
                    rs.getString("firmName"),
                    rs.getString("manager"),
                    rs.getInt("tables")));
                }
            }

            return workspaces;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getCities() {
        try(
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select distinct city from workspaces");
        ) {

            List<String> cities = new ArrayList<>();

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                cities.add(rs.getString("city"));
            }

            return cities;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Workspace getWorkspace(String name) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from workspaces where name = ?");
        ) {
            Workspace workspace = null;

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                workspace = new Workspace(
                rs.getInt("idWorkspace"),
                rs.getString("name"),
                rs.getString("city"),
                rs.getInt("likes"),
                rs.getBoolean("activeStatus"),
                rs.getString("adress"), 
                rs.getString("firmName"),
                rs.getString("manager"),
                rs.getInt("tables"));
            }

            return workspace;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getNumOfWorkspaces() {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from workspaces");
        ) {
            int res = 0;

            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                res++;
            }

            return res;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
}
