package com.example.backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.db.dao.ReservationRepo;
import com.example.backend.models.Reservation;
import com.example.backend.models.User;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:4200/")
public class ReservationController {
    
    @PostMapping("getReservationsForUser")
    public List<Reservation> getAllUsers(@RequestBody User user) {
        return new ReservationRepo().getAllReservationsForUser(user);
    }
    

}
