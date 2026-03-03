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
import com.example.backend.models.User;
import com.example.backend.models.Workspace;

public class ReservationRepo implements ReservationRepoInterface{

    @Override
    public List<Reservation> getAllReservationsForUser(User user) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from reservations where user = ?");
        ) {

            pstmt.setString(1, user.getUsername());

            List<Reservation> reservations = new ArrayList<>();

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                reservations.add(new Reservation(
                    rs.getInt("idReservation"),
                    rs.getString("roomName"),
                    rs.getString("user"),
                    rs.getString("workspaceName"),
                    rs.getString("city"),
                    rs.getString("startDate"),
                    rs.getString("endDate"),
                    rs.getBoolean("active")));
            }

            return reservations;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reservation> getAllReservationsForRoom(Room room) {
        Workspace workspace = new WorkspaceRepo().getWorkspaceWithId(room.getIdWorkspace());
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from reservations where roomName = ? and workspaceName = ? and city = ?");
        ) {

            pstmt.setString(1, room.getName());
            pstmt.setString(2, workspace.getName());
            pstmt.setString(3, workspace.getCity());

            List<Reservation> reservations = new ArrayList<>();

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                reservations.add(new Reservation(
                    rs.getInt("idReservation"),
                    rs.getString("roomName"),
                    rs.getString("user"),
                    rs.getString("workspaceName"),
                    rs.getString("city"),
                    rs.getString("startDate"),
                    rs.getString("endDate"),
                    rs.getBoolean("active")));
            }

            return reservations;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int postReservation(Reservation reservation) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("insert into reservations (idReservation, roomName, user, workspaceName, city, startDate, endDate, active) values (?, ?, ?, ?, ?, ?, ?, false)");
        ) {
            pstmt.setInt(1, reservation.getIdReservation());
            pstmt.setString(2, reservation.getRoomName());
            pstmt.setString(3, reservation.getUser());
            pstmt.setString(4, reservation.getWorkspaceName());
            pstmt.setString(5, reservation.getCity());
            pstmt.setString(6, reservation.getStartDate());
            pstmt.setString(7, reservation.getEndDate());

            pstmt.executeUpdate();
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    
    
}
