package com.example.backend.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.backend.db.DB;
import com.example.backend.models.Reservation;
import com.example.backend.models.User;

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
    
}
