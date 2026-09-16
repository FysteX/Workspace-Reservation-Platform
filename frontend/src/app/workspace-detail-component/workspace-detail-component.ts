import { Component, inject } from '@angular/core';
import { StorageService } from '../services/storage-service';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { Room } from '../models/room';
import { Workspace } from '../models/workspace';
import { RoomService } from '../services/room-service';
import { Reservation } from '../models/reservation';
import { ReservationService } from '../services/reservation-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-workspace-detail-component',
  imports: [FormsModule,
    MatFormFieldModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule],
  templateUrl: './workspace-detail-component.html',
  styleUrl: './workspace-detail-component.css',
})
export class WorkspaceDetailComponent {
  
  storageService = inject(StorageService)
  userService = inject(UserService)
  roomService = inject(RoomService)
  reservationService = inject(ReservationService)
  router = inject(Router)
  
  manager: User = new User()

  message = ""

  selectedDate: Date = new Date()
  today = Date.now()
  selectedDateString = ""
  startHour = ""
  startMin = ""
  endHour = ""
  endMin = ""
  dateSelected = false;
  reservingMessage = ""

  adequateRooms: Room[] = []
  currentRoomName = ""
  currentRoomNumber = 0
  selectedWorkspace: Workspace = new Workspace()
  workspaceArrayForService: Workspace[] = []
  roomType = ""
  officeTables = 0
  reservationsForRoom: Reservation[] = []

  getReservationsForRoom(room: Room) {
    this.reservationService.getReservationsForRoom(room).subscribe(data => {
      if(data != null) {
        this.reservationsForRoom = data
      } else {
        alert("problem sa bekom, getReservationsForRoom")
      }
    })
  }

  ngOnInit() {
    this.manager.username = this.storageService.selectedWorkspace.manager
    if(this.storageService.enableReservation) {
      this.selectedWorkspace = this.storageService.selectedWorkspace
      this.workspaceArrayForService[0] = this.selectedWorkspace
      this.roomType = this.storageService.roomType
      this.officeTables = this.storageService.officeTables

      if(this.roomType == "table") {
        for(let i: number = 0 ; i < this.selectedWorkspace.tables ; i++) {
          let table: Room = new Room()
          table.idWorkspace = this.selectedWorkspace.idWorkspace
          table.name = "t" + i.toString()
          this.adequateRooms.push(table)
        }
      } else {
        this.roomService.getRoomsForWorkspaces(this.workspaceArrayForService).subscribe(data => {
          if(data != null) {
            for(let i: number = 0 ; i < data.length ; i++) {
              if(data[i].type == this.roomType) {
                if(this.roomType == "office" && data[i].tables < this.officeTables) {
                  continue
                }
                this.adequateRooms.push(data[i])
              }
            }
            this.getReservationsForRoom(this.adequateRooms[0])
            this.currentRoomName = this.adequateRooms[0].name
          }
        })
      }
    }

    this.userService.getUser(this.manager).subscribe(data => {
      if(data != null) {
        this.manager = data
      }
    })
  }

  isEnteredTimeOverlapingReservation(reservation: Reservation) {
    let enteredStart = parseInt(this.startHour) * 60 + parseInt(this.startMin)
    let enteredEnd = parseInt(this.endHour) * 60 + parseInt(this.endMin)

    let reservationStartHour = parseInt(reservation.startTime.split(":")[0])
    let reservationStartMin = parseInt(reservation.startTime.split(":")[1])

    let reservationEndHour = parseInt(reservation.endTime.split(":")[0])
    let reservationEndMin = parseInt(reservation.endTime.split(":")[1])

    let reservationStart = reservationStartHour * 60 + reservationStartMin
    let reservationEnd = reservationEndHour * 60 + reservationEndMin

    let overlap = enteredStart < reservationEnd && enteredEnd > reservationStart

    if (overlap) {
      return true
    } else {
      return false
    }
  }

  reserve() {
    this.reservingMessage = ""
    if(this.startHour.length == 0 || this.startMin.length == 0 || this.endHour.length == 0 || this.endMin.length == 0) {
      this.reservingMessage = "You must enter all fields."
    } else if(parseInt(this.startHour) < 0 || parseInt(this.startMin) < 0 || parseInt(this.endHour) < 0 || parseInt(this.endMin) < 0 || parseInt(this.startHour) > 23 || parseInt(this.startMin) > 59 || parseInt(this.endHour) > 23 || parseInt(this.endMin) > 59) {
      this.reservingMessage = "Enter correct values for time."
    } else if(parseInt(this.startHour) > parseInt(this.endHour) || (parseInt(this.startHour) == parseInt(this.endHour) && parseInt(this.startMin) >= parseInt(this.endMin))) {
      this.reservingMessage = "End time must be later than start time."
    } else {
      for(let i: number = 0 ; i < this.reservationsForRoom.length ; i++) {
        if(this.reservationsForRoom[i].date == this.selectedDateString && this.isEnteredTimeOverlapingReservation(this.reservationsForRoom[i])) {
          this.reservingMessage = "Entered time is overlaping with existing reservation."
          return;
        } 
      }

      let reservation: Reservation = new Reservation()
      let startTime = this.startHour.toString().padStart(2, "0") + ":" + this.startMin.toString().padStart(2, "0")
      let endTime = this.endHour.toString().padStart(2, "0") + ":" + this.endMin.toString().padStart(2, "0")

      reservation.idReservation = 0
      reservation.roomName = this.adequateRooms[this.currentRoomNumber].name
      reservation.user = this.storageService.loggedinUser.username
      reservation.workspaceName = this.selectedWorkspace.name
      reservation.city = this.selectedWorkspace.city
      reservation.date = this.selectedDateString
      reservation.startTime = startTime
      reservation.endTime = endTime
      reservation.active = false

      this.reservationService.postReservation(reservation).subscribe(data => {
        if(data > 0) {
          this.getReservationsForRoom(this.adequateRooms[this.currentRoomNumber])
        }
      })
    }
  }

  changeRoom(increment: number) {
    this.currentRoomNumber += increment
    if(this.currentRoomNumber < 0) {
      this.currentRoomNumber = this.adequateRooms.length - 1
    } else if (this.currentRoomNumber == this.adequateRooms.length) {
      this.currentRoomNumber = 0;
    }
    this.getReservationsForRoom(this.adequateRooms[this.currentRoomNumber])
    this.currentRoomName = this.adequateRooms[this.currentRoomNumber].name
  }

  showReservations() {
    if(!this.dateSelected) {
      this.dateSelected = true
    }
    this.reservingMessage = ""
    this.selectedDateString = this.reservationService.formStringFromDate(this.selectedDate)
  }

  logout() {
    this.router.navigate(["login"])
  }

  backToUserPage() {
    this.router.navigate(["member"])
  }
}
