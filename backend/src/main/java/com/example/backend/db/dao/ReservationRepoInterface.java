package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Reservation;
import com.example.backend.models.Room;
import com.example.backend.models.User;
import com.example.backend.models.Workspace;

public interface ReservationRepoInterface {
    
    List<Reservation> getAllReservationsForUser(User user); 

    List<Reservation> getAllReservationsForRoom(Room room);

    List<Reservation> getAllReservationsForWorkspace(Workspace workspace);

    int postReservation(Reservation reservation);

    List<Reservation> getAllReservationsForManager(User manager);

    int updateReservation(Reservation reservation);

}
