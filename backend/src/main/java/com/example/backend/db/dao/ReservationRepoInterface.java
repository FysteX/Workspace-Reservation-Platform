package com.example.backend.db.dao;

import java.util.List;

import com.example.backend.models.Reservation;
import com.example.backend.models.User;

public interface ReservationRepoInterface {
    
    List<Reservation> getAllReservationsForUser(User user); 

}
