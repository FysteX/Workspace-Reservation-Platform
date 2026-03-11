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
                    rs.getString("date"),
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getBoolean("active"),
                    rs.getString("showedUp")));
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
                    rs.getString("date"),
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getBoolean("active"),
                    rs.getString("showedUp")));
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
            PreparedStatement pstmt = conn.prepareStatement("insert into reservations (idReservation, roomName, user, workspaceName, city, date, startTime, endTime, active) values (?, ?, ?, ?, ?, ?, ?, ?, false)");
        ) {
            pstmt.setInt(1, reservation.getIdReservation());
            pstmt.setString(2, reservation.getRoomName());
            pstmt.setString(3, reservation.getUser());
            pstmt.setString(4, reservation.getWorkspaceName());
            pstmt.setString(5, reservation.getCity());
            pstmt.setString(6, reservation.getDate());
            pstmt.setString(7, reservation.getStartTime());
            pstmt.setString(8, reservation.getEndTime());

            pstmt.executeUpdate();
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public List<Reservation> getAllReservationsForManager(User manager) {
        List<Workspace> workspaces = new WorkspaceRepo().getWorkspacesForManager(manager);

        List<Reservation> reservations = new ArrayList<>();

        for(Workspace workspace : workspaces) {
            reservations.addAll(getAllReservationsForWorkspace(workspace));
        }

        return reservations;
    }

    @Override
    public List<Reservation> getAllReservationsForWorkspace(Workspace workspace) {
       try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from reservations where workspaceName = ? and city = ?");
        ) {

            pstmt.setString(1, workspace.getName());
            pstmt.setString(2, workspace.getCity());

            List<Reservation> reservations = new ArrayList<>();

            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                reservations.add(new Reservation(
                    rs.getInt("idReservation"),
                    rs.getString("roomName"),
                    rs.getString("user"),
                    rs.getString("workspaceName"),
                    rs.getString("city"),
                    rs.getString("date"),
                    rs.getString("startTime"),
                    rs.getString("endTime"),
                    rs.getBoolean("active"),
                    rs.getString("showedUp")));
            }

            return reservations;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int updateReservation(Reservation reservation) {
        try (
            Connection conn = DB.source().getConnection();
            PreparedStatement pstmt = conn.prepareStatement("update reservations set roomName = ?, user = ?, workspaceName = ?, city = ?, date = ?, startTime = ?, endTime = ?, active = ?, showedUp = ? where idReservation = ?");
        ) {
            
            pstmt.setString(1, reservation.getRoomName());
            pstmt.setString(2, reservation.getUser());
            pstmt.setString(3, reservation.getWorkspaceName());
            pstmt.setString(4, reservation.getCity());
            pstmt.setString(5, reservation.getDate());
            pstmt.setString(6, reservation.getStartTime());
            pstmt.setString(7, reservation.getEndTime());
            pstmt.setBoolean(8, reservation.isActive());
            pstmt.setString(9, reservation.getShowedUp());
            pstmt.setInt(10, reservation.getIdReservation());

            pstmt.executeUpdate();
            return 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    
    
}
