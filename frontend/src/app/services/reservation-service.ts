import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/user';
import { Reservation } from '../models/reservation';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root',
})
export class ReservationService {
  
  http = inject(HttpClient)

  url = "http://localhost:8080/reservations";

  getReservationsForUser(user: User) {
    return this.http.post<Reservation[]>(this.url + "/getReservationsForUser", user);
  }

  getReservationsForRoom(room: Room) {
    return this.http.post<Reservation[]>(this.url + "/getReservationsForRoom", room);
  }

  postReservation(reservation: Reservation) {
    return this.http.post<number>(this.url + "/postReservation", reservation)
  }

  getReservationsForManager(manager: User) {
    return this.http.post<Reservation[]>(this.url + "/getReservationsForManager", manager)
  }

  formStringFromDate(date: Date) {
    let year = date.getFullYear()
    let month = String(date.getMonth() + 1).padStart(2, "0")
    let day = String(date.getDate()).padStart(2, "0")

    let formatted = `${year}-${month}-${day}`

    return formatted
  }

  updateReservation(reservation: Reservation) {
    return this.http.post<number>(this.url + "/updateReservation", reservation)
  }

}
