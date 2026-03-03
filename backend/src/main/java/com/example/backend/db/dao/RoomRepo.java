package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Reservation;
import com.example.backend.models.Room;
import com.example.backend.models.Workspace;

public class RoomRepo implements RoomRepoInterface {

    @Override
    public List<Room> getRoomsForWorkspace(Workspace[] workspaces) {
       try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from rooms where idWorkspace = ?");
        ) {

            List<Room> rooms = new ArrayList<>();

            for(int i = 0 ; i < workspaces.length ; i++) {
                pstmt.setInt(1, workspaces[i].getIdWorkspace());
                ResultSet rs = pstmt.executeQuery();
            
                while(rs.next()) {
                    rooms.add(new Room(
                        rs.getInt("idWorkspace"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("tables"),
                        rs.getString("description")));
                }
            }
            
            return rooms;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int postRoom(Room room) {
       try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into rooms (idWorkspace, name, type, tables, description) values (?, ?, ?, ?, ?)");
        ) {
            pstmt.setInt(1, room.getIdWorkspace());
            pstmt.setString(2, room.getName());
            pstmt.setString(3, room.getType());
            pstmt.setInt(4, room.getTables());
            pstmt.setString(5, room.getDescription());

            pstmt.executeUpdate();
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}

