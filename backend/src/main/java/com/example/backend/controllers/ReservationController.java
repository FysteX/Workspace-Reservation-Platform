package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ReservationRepo;
import com.example.backend.models.Reservation;
import com.example.backend.models.Room;
import com.example.backend.models.User;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:4200/")
public class ReservationController {
    
    @PostMapping("getReservationsForUser")
    public List<Reservation> getReservationsForUser(@RequestBody User user) {
        return new ReservationRepo().getAllReservationsForUser(user);
    }
    
    @PostMapping("getReservationsForRoom")
    public List<Reservation> getReservationsForRoom(@RequestBody Room room) {
        return new ReservationRepo().getAllReservationsForRoom(room);
    }

    @PostMapping("postReservation")
    public int postReservation(@RequestBody Reservation reservation) {
        return new ReservationRepo().postReservation(reservation);
    }

}
