import { Component, inject } from '@angular/core';
import { StorageService } from '../services/storage-service';
import { UserService } from '../services/user.service';
import { FormsModule } from '@angular/forms';
import { WorkspaceService } from '../services/workspace-service';
import { Workspace } from '../models/workspace';
import { RoomService } from '../services/room-service';
import { User } from '../models/user';
import { Room } from '../models/room';
import { ReservationService } from '../services/reservation-service';
import { Reservation } from '../models/reservation';
import { FullCalendarModule } from '@fullcalendar/angular';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import jsPDF from 'jspdf';
import { PictureService } from '../services/picture-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-manager-component',
  imports: [FormsModule, FullCalendarModule],
  templateUrl: './manager-component.html',
  styleUrl: './manager-component.css',
})
export class ManagerComponent {

  storageService = inject(StorageService)
  userService = inject(UserService)
  workspaceService = inject(WorkspaceService)
  roomService = inject(RoomService)
  reservationService = inject(ReservationService)
  pictureService = inject(PictureService)
  router = inject(Router)

  loggedinUser: User = this.storageService.loggedinUser
  selectedOption = -1
  message = ""
  workspaces: Workspace[] = []

  selectedSubOption = -1;

  //adding element variables
  newWorkspace: Workspace = new Workspace()
  newOffice: Room = new Room()
  newConferenceHall: Room = new Room()
  workspaceAdded = false
  workspaceMessage = ""
  officeMessage = ""
  conferenceHallMessage = ""

  //updating element variables
  updatingWorkspace: Workspace = new Workspace()
  updatingOffice: Room = new Room()
  updatingConferenceHall: Room = new Room()
  updateOption = -1
  updateWorkspaceMessage = ""
  updateOfficeMessage = ""
  updateConferenceHallMessage = "" 
  
  currentTime: Date = new Date()

  reservations: Reservation[] = []

  //calendar variables
  selectedRoom = new Room()
  reservationsForRoom: Reservation[] = []

  //pdf variables
  month = ""
  year = ""

  profilePictureUrl = ""

  showSection(param: number) {
    this.selectedOption = param;
    
    if(param == 2) {
      this.currentTime = new Date()
    }
  }

  logout() {
    this.storageService.loggedinUser = new User()
    this.router.navigate(["login"])
  }

  numSequence(n: number): Array<number> {
    return Array(n).fill(0).map((x, i) => i);
  }

  ngOnInit() {
    this.workspaceService.getWorkspacesForManager(this.loggedinUser).subscribe(data => {
      if(data != null) {
        this.workspaces = data
        let workspace: Workspace[] = []
        for(let i: number = 0 ; i < this.workspaces.length ; i++) {
          workspace[0] = this.workspaces[i]
          this.roomService.getRoomsForWorkspaces(workspace).subscribe(data =>{
            if(data != null) {
              this.workspaces[i].elements = data
            } else {
              alert("Greska sa bekom, getRoomsForWorkspaces")
            }
          })
        }
      } else {
        alert("Greska sa bekom, getWorkspacesForManager")
      }
    })

    this.getPicture()
    
    this.reservationService.getReservationsForManager(this.loggedinUser).subscribe(data => {
      if(data != null) {
        this.reservations = data
      } else {
        alert("Problem sa bekom, getReservationsForManager")
      }
    })
  }

  changeUserData() {
    if(this.userService.validatePassword(this.loggedinUser.password)) {
      this.message = "The password must have a minimum of 8 characters, a maximum of 12 characters, including at least one uppercase letter, one number and one special character, and must start with a letter."
    }
    else {
      this.userService.updateUser(this.loggedinUser).subscribe(data => {
        if(this.selectedFile != null) {
          this.pictureService.postPicture(this.selectedFile, data).subscribe(data2 => {
            this.getPicture()
            alert("User data succesfuly changed")
          })
        }
      })
      this.message = ""
    }
    
  }

  add() {
    this.selectedSubOption = 1
  }

  addWorkspace() {
    this.newWorkspace.firmName = this.loggedinUser.firmName
    this.newWorkspace.manager = this.loggedinUser.username
    this.workspaceMessage = ""
    if(this.newWorkspace.tables < 5) {
      this.workspaceMessage = "Open space must have at least 5 tables"
      return
    } else if(this.newWorkspace.price < 0 || this.newWorkspace.tables < 0) {
      this.workspaceMessage = "You can't enter negative values"
      return
    }

    this.workspaceService.postWorkspace(this.newWorkspace).subscribe(data => {
      if(data > 0) {
        alert("Workspace added, now you can add offices and conference halls")
        this.newWorkspace.idWorkspace = data
        this.workspaceAdded = true
      } else if(data == 0) {
        this.workspaceMessage = "Workspace in entered city with entered name already exists."
      } else if(data == -1) {
        alert("Problem sa bekom, postWorkspace")
      }
    })
  }

  addOffice(workspace: Workspace) {
    this.officeMessage = ""
    if(this.newOffice.tables < 1) {
      this.officeMessage = "Office must contain at least 1 table."
      return
    }

    this.newOffice.type = "office"
    this.newOffice.idWorkspace = workspace.idWorkspace

    this.roomService.postRoom(this.newOffice).subscribe(data => {
      if (data == 1) {
        alert("Office added")
        if(this.selectedSubOption == 0) {
          this.getWorkspacesForManager()
        }
      } else if (data == 0) {
        this.officeMessage = "Office with entered name already exist in that workspace."
      } else if (data == -1) {
        alert("Problem sa bekom, postRoom office")
      }
    })
  }

  addConferenceHall(workspace: Workspace) {
    this.conferenceHallMessage = ""
    if(this.newConferenceHall.description.length > 300) {
      this.conferenceHallMessage = "Conference hall description must be less than 300 characters"
      return
    }

    this.newConferenceHall.type = "conference hall"
    this.newConferenceHall.idWorkspace = workspace.idWorkspace

    this.roomService.postRoom(this.newConferenceHall).subscribe(data => {
      if (data == 1) {
        alert("Conference hall added")
        if(this.selectedSubOption == 0) {
          this.getWorkspacesForManager()
        }
      } else if (data == 0) {
        this.conferenceHallMessage = "Conference hall with entered name already exist in that workspace."
      } else if (data == -1) {
        alert("Problem sa bekom, postRoom conference hal")
      }
    })
  }

  getWorkspacesForManager() {
    this.workspaceService.getWorkspacesForManager(this.loggedinUser).subscribe(data => {
      if(data != null) {
        this.workspaces = data
        let workspace: Workspace[] = []
        for(let i: number = 0 ; i < this.workspaces.length ; i++) {
          workspace[0] = this.workspaces[i]
          this.roomService.getRoomsForWorkspaces(workspace).subscribe(data =>{
            if(data != null) {
              this.workspaces[i].elements = data
            } else {
              alert("Greska sa bekom, getRoomsForWorkspaces")
            }
          })
        }
      } else {
        alert("Greska sa bekom, getWorkspacesForManager")
      }
    })
  }

   update() {
    this.selectedSubOption = 0

    this.getWorkspacesForManager()     
  }

  showUpdateWorkspaceSection(workspace: Workspace) {
    this.updatingWorkspace = {...workspace}
    this.updateOption = 0
  } 

  showUpdateOfficeSection(office: Room) {
    this.updatingOffice = {...office}
    this.updateOption = 1
  }

  showUpdateConferenceHallSection(conferenceHall: Room) {
    this.updatingConferenceHall = {...conferenceHall}
    this.updateOption = 2
  }

  changeUpdateOption(option: number, workspace: Workspace) {
    this.updatingWorkspace = {...workspace}
    this.updateOption = option
  }

  updateWorkspace() {
    this.updateWorkspaceMessage = ""
    if(this.updatingWorkspace.tables < 5) {
      this.updateWorkspaceMessage = "Open space must have at least 5 tables"
      return
    } else if(this.updatingWorkspace.price < 0 || this.updatingWorkspace.tables < 0) {
      this.updateWorkspaceMessage = "You can't enter negative values"
      return
    }

    this.workspaceService.updateWorkspace(this.updatingWorkspace).subscribe(data => {
      if(data > 0) {
        alert("Updated " + data.toString())
        this.updatingWorkspace.idWorkspace = data
        this.getWorkspacesForManager()     
      } else if(data == 0) {
        this.updateWorkspaceMessage = "Workspace in entered city with entered name already exists."
      } else if(data == -1) {
        alert("Problem sa bekom, updateWorkspace")
      }
    })
  }

  updateOffice() {
    this.updateOfficeMessage = ""
    if(this.updatingOffice.tables < 1) {
      this.updateOfficeMessage = "Office must contain at least 1 table."
      return
    }

    this.roomService.updateRoom(this.updatingOffice).subscribe(data => {
      if (data == 1) {
        alert("Office updated")
        this.getWorkspacesForManager()     
      } else if (data == 0) {
        this.updateOfficeMessage = "Office with entered name already exist in that workspace."
      } else if (data == -1) {
        alert("Problem sa bekom, updateRoom office")
      }
    })
  }

  updateConferenceHall() {
    this.updateConferenceHallMessage = ""
    if(this.updatingConferenceHall.description.length > 300) {
      this.updateConferenceHallMessage = "Conference hall description must be less than 300 characters"
      return
    }

    this.roomService.updateRoom(this.updatingConferenceHall).subscribe(data => {
      if (data == 1) {
        alert("Conference hall updated")
        this.getWorkspacesForManager()     
      } else if (data == 0) {
        this.updateConferenceHallMessage = "Conference hall with entered name already exist in that workspace."
      } else if (data == -1) {
        alert("Problem sa bekom, postRoom conference hal")
      }
    })
  }

  onFileSelected(event: any) {
    const file = event.target.files[0]
    if (!file) return

    const reader = new FileReader()

    reader.onload = () => {
      this.workspaceMessage = ""
      try {
        const rawData = JSON.parse(reader.result as string)

        let workspace = new Workspace()

        workspace.name = rawData.name
        workspace.city = rawData.city
        workspace.adress = rawData.adress
        workspace.price = rawData.price
        workspace.tables = rawData.tables
        workspace.firmName = this.loggedinUser.firmName
        workspace.manager = this.loggedinUser.username

        this.workspaceService.postWorkspace(workspace).subscribe(data => {
          if(data > 0) {
            for(let i: number = 0 ; i < rawData.elements.length ; i++) {
              let room = new Room()

              room.idWorkspace = data
              room.name = rawData.elements[i].name
              room.type = rawData.elements[i].type
              room.tables = rawData.elements[i].tables
              room.description = rawData.elements[i].description

              this.roomService.postRoom(room).subscribe(data => {
                if (data == -1) {
                  this.workspaceMessage = "You entered 2 rooms with same type and name, one of them will be added"
                }
              })
            }  
          }else if(data == 0) {
            this.workspaceMessage = "Workspace in entered city with entered name already exists."
          } else if(data == -1) {
            alert("Problem sa bekom, postWorkspace JSON file")
          }
        })
      } catch (error) {
        alert("Invalid JSON file!") //test ovoga
      }
    }
    reader.readAsText(file);
  }

  reservationHasBegun(reservation: Reservation){
    let startHour = parseInt(reservation.startTime.substring(0,2))
    let startMinute = parseInt(reservation.startTime.substring(3,5)) + startHour * 60

    if(reservation.date == this.reservationService.formStringFromDate(this.currentTime) && 
      this.currentTime.getHours()*60 + this.currentTime.getMinutes() - startMinute <= 10 && 
      this.currentTime.getHours()*60 + this.currentTime.getMinutes() - startMinute >= 0) {
        return true
    }
    return false
  }

  setShowedUp(reservation: Reservation, showedUp: boolean) {

    if(showedUp) {
      reservation.showedUp = "yes"
    } else {
      reservation.showedUp = "no"
    }
    reservation.active = false
    
    this.reservationService.updateReservation(reservation).subscribe()
  }

  calendarOptions = {
    initialView: 'timeGridWeek',
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],

    editable: true, 
    eventOverlap: false,

    events: [
      {
        id: '0',
        title: 'Reservation',
        start: '2026-06-10T10:00:00',
        end: '2026-06-10T12:00:00',
        extendedProps: {
          user: 'pera',
          workspaceName: 'w1',
          city: 'Belgrade',
          active: false,
        }
      }
    ],

    eventDrop: (info: any) => {
      let reservation = new Reservation()

      reservation.idReservation = parseInt(info.event.id)
      reservation.roomName = info.event.title
      reservation.user = info.event.extendedProps.user
      reservation.city = info.event.extendedProps.city
      reservation.workspaceName = info.event.extendedProps.workspaceName
      reservation.active = info.event.extendedProps.active
      reservation.date = this.reservationService.formStringFromDate(info.event.start)
      reservation.startTime = info.event.start.getHours().toString().padStart(2, "0") + ":" + info.event.start.getMinutes().toString().padStart(2, "0") 
      reservation.endTime = info.event.end.getHours().toString().padStart(2, "0") + ":" + info.event.end.getMinutes().toString().padStart(2, "0") 

      this.reservationService.updateReservation(reservation).subscribe(data => {

      })
    }
  };

  onSelectionChange(event: any) {
    let array = event.target.value.split(";")

    let idWorkspace = array[0]
    let roomName = array[1]

    let room: Room = new Room()

    room.name = roomName
    room.idWorkspace = idWorkspace

    this.reservationService.getReservationsForRoom(room).subscribe(data => {
      if(data) {
        this.calendarOptions.events = data.map(r => ({
        id: r.idReservation.toString(),
        title: r.roomName,
        start: `${r.date}T${r.startTime}:00`,
        end: `${r.date}T${r.endTime}:00`,
        extendedProps: {
          user: r.user,
          workspaceName: r.workspaceName,
          city: r.city,
          active: r.active
  }
    }));
      } else {
        alert("Greska sa bekom, getReservationsForRoom")
      }
    })
  }

  generatePdfReport() {

    let doc = new jsPDF()

    let minutes = 0
    let totalReservations = 0
    let usagePercent
    let daysOfMonth
    let numOfRooms = 0
    let rooms: Room[] = []

    for(let i: number = 0; i < this.reservations.length ; i++) {
      if(this.reservations[i].date.substring(0, 4) == this.year && this.reservations[i].date.substring(6, 7) == this.month){
        totalReservations++
        let startHour = parseInt(this.reservations[i].startTime.substring(0,2)) 
        let endHour =  parseInt(this.reservations[i].endTime.substring(0,2))

        let startMinutes = parseInt(this.reservations[i].startTime.substring(3,5))
        let endMinutes = parseInt(this.reservations[i].endTime.substring(3,5))

        minutes += (endHour * 60 + endMinutes) - (startHour * 60 + startMinutes)
      }
    }

    if(this.month == "1" || this.month == "3" || this.month == "5" || this.month == "7" || this.month == "8" || this.month == "10"
      || this.month == "12") {
        daysOfMonth = 31
    } else if (this.month == "4" || this.month == "6" || this.month == "9" || this.month == "11") {
      daysOfMonth = 30
    } else {
      daysOfMonth = 28
    }

    this.roomService.getRoomsForWorkspaces(this.workspaces).subscribe(data => {
      if(data) {
        numOfRooms += data.length
        for(let i: number = 0; i < this.workspaces.length ; i++) {
          numOfRooms += this.workspaces[i].tables
        }

        usagePercent = (minutes / (daysOfMonth*numOfRooms*24*60*1.0)) * 100

        doc.setFontSize(18)
        doc.text("Workspace Monthly Report", 20, 20)

        doc.setFontSize(12)
        doc.text("Month: " + this.year + "-" + this.month, 20, 50)

        doc.text("Total reservations: " + totalReservations, 20, 70)
        doc.text("Capacity usage: " + usagePercent + "%", 20, 80)

        doc.save("monthly_report.pdf")
      } else {
        alert("Problem sa bekom, getRoomsForWorkspaces")
      }
    })
  }

  selectedFile!: File

  onPictureSelected(event: any) {
    const file = event.target.files[0]

    if (!file) return

    if (!['image/jpeg', 'image/png'].includes(file.type)) {
      this.message = "Profile picture must be in JPG or PNG format"
      return;
    }

    const img = new Image();
    img.onload = () => {
      if (img.width < 100 || img.height < 100 || img.width > 300 || img.height > 300) {
        this.message = "Picture must be between 100x100 and 300x300 px"
        return;
      }

      this.selectedFile = file
    }

    img.src = URL.createObjectURL(file)
  }

  getPicture() {
     this.pictureService.getPicture(this.loggedinUser.idPicture == -1 ? "deffault" : this.loggedinUser.idPicture.toString()).subscribe(data => {
      this.profilePictureUrl = URL.createObjectURL(data)
    })
  }

}
