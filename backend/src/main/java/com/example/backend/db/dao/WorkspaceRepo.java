package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.User;
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
                    rs.getInt("tables"),
                    rs.getInt("price")));
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
                rs.getInt("tables"),
                rs.getInt("price"));
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

    @Override
    public Workspace getWorkspaceWithId(int id) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from workspaces where idWorkspace = ?");
        ) {
            Workspace workspace = null;

            pstmt.setInt(1, id);
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
                rs.getInt("tables"),
                rs.getInt("price"));
            }

            return workspace;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Workspace> getWorkspacesForManager(User user) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from workspaces where manager = ?");
        ) {
            List<Workspace> workspaces = new ArrayList<>();

            pstmt.setString(1, user.getUsername());
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
                rs.getInt("tables"),
                rs.getInt("price")));
            }

            return workspaces;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getWorkspaceForNameAndCity(String name, String city) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from workspaces where name = ? and city = ?");
        ) {
            pstmt.setString(1, name);
            pstmt.setString(2, city);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                return rs.getInt("idWorkspace");
            }

            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int postWorkspace(Workspace workspace) {
        if(getWorkspaceForNameAndCity(workspace.getName(), workspace.getCity()) > 0) {
            return 0;
        }
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into workspaces (name, city, activeStatus, adress, firmName, manager, tables, price) values (?, ?, false, ?, ?, ?, ?, ?)");
        ) {
            pstmt.setString(1, workspace.getName());
            pstmt.setString(2, workspace.getCity());
            pstmt.setString(3, workspace.getAdress());
            pstmt.setString(4, workspace.getFirmName());
            pstmt.setString(5, workspace.getManager());
            pstmt.setInt(6, workspace.getTables());
            pstmt.setInt(7, workspace.getPrice());


            pstmt.executeUpdate();
            return getWorkspaceForNameAndCity(workspace.getName(), workspace.getCity());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int updateWorkspace(Workspace workspace) {
        int existingWorkspaceId = getWorkspaceForNameAndCity(workspace.getName(), workspace.getCity());
        if(existingWorkspaceId > 0 && existingWorkspaceId != workspace.getIdWorkspace()) {
            //ako postoji neki drugi prostor sa istim imenom u istom gradu
            return 0;
        }
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update workspaces set name = ?, city = ?, adress = ?, tables = ?, price = ?, activeStatus = ? where idWorkspace = ?");
        ) {

            pstmt.setString(1, workspace.getName());
            pstmt.setString(2, workspace.getCity());
            pstmt.setString(3, workspace.getAdress());
            pstmt.setInt(4, workspace.getTables());
            pstmt.setInt(5, workspace.getPrice());
            pstmt.setBoolean(6, workspace.isActiveStatus());
            pstmt.setInt(7, workspace.getIdWorkspace());
            
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<Workspace> getAllWorkspaces() {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from workspaces");
        ) {
            List<Workspace> workspaces = new ArrayList<>();

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
                rs.getInt("tables"),
                rs.getInt("price")));
            }

            return workspaces;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int deleteWorkspace(Workspace workspace) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("delete from workspaces where idWorkspace = ?");
        ) {

            pstmt.setInt(1, workspace.getIdWorkspace());

            int res = pstmt.executeUpdate();

            if(res > 0) {
                new RoomRepo().deleteRoomsForWorkspace(workspace);
            }

            return res;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
