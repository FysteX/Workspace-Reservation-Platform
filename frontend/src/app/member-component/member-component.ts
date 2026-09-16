import { Component, inject } from '@angular/core';
import { StorageService } from '../services/storage-service';
import { User } from '../models/user';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { Reservation } from '../models/reservation';
import { ReservationService } from '../services/reservation-service';
import { Workspace } from '../models/workspace';
import { WorkspaceService } from '../services/workspace-service';
import { RoomService } from '../services/room-service';
import { Room } from '../models/room';
import { Router } from '@angular/router';
import { PictureService } from '../services/picture-service';

@Component({
  selector: 'app-member-component',
  imports: [FormsModule],
  templateUrl: './member-component.html',
  styleUrl: './member-component.css',
})
export class MemberComponent {

  storageService = inject(StorageService)
  userService = inject(UserService)
  reservationService = inject(ReservationService)
  workspaceService = inject(WorkspaceService)
  roomService = inject(RoomService)
  router = inject(Router)
  pictureService = inject(PictureService)

  loggedinUser: User = new User()

  reservations: Reservation[] = []

  selectedOption = -1
  message = ""

  cities: string[] = []
  selectedCities: string[] = []
  searchInput = ""
  temporarySearchResults : Workspace[] = []
  searchResults : Workspace[] = []
  searchResultMessage = ""
  roomType = ""
  officeTables = 0

  profilePictureUrl = ""

  ngOnInit() {
    this.loggedinUser = this.storageService.loggedinUser
    this.getPicture()
    this.reservationService.getReservationsForUser(this.loggedinUser).subscribe(data => {
      if(data != null) {
        this.reservations = data;
      } else {
        alert("Greska sa bekom, getReservationsForUser()")
      }
    })
    this.workspaceService.getCities().subscribe(data => {
      if (data!= null) {
        this.cities = data;
      }
    })
  }

  logout() {
    this.storageService.loggedinUser = new User()
    this.router.navigate(["login"])
  }

  getPicture() {
     this.pictureService.getPicture(this.loggedinUser.idPicture == -1 ? "deffault" : this.loggedinUser.idPicture.toString()).subscribe(data => {
      this.profilePictureUrl = URL.createObjectURL(data)
    })
  }

  selectedFile!: File

  onFileSelected(event: any) {
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

  showSection(param: number) {
    this.selectedOption = param;
  }

  sortReservations() {
    this.reservations.sort((a,b) => a.workspaceName.localeCompare(b.workspaceName))
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

  searchRoomsForWorkspaces() {
    this.roomService.getRoomsForWorkspaces(this.temporarySearchResults).subscribe(data => {
      for(let i: number = 0 ; i < this.temporarySearchResults.length ; i++) {
        for(let j: number = 0 ; j < data.length ; j++) {
          if(this.temporarySearchResults[i].idWorkspace == data[j].idWorkspace && this.roomType == data[j].type) {
            if(this.roomType == "office" && data[j].tables >= this.officeTables) {
              this.searchResults.push(this.temporarySearchResults[i])
              break
            } else if (this.roomType == "conference hall") {
              this.searchResults.push(this.temporarySearchResults[i])
              break
            }
          }
        }
      }
    })
  }
  
  search() {
    this.temporarySearchResults = []
    this.searchResults = []
    this.searchResultMessage = ""
    if(this.selectedCities.length > 0 && this.searchInput.length > 0) {
      this.workspaceService.getWorkspacesForCities(this.selectedCities).subscribe(data => {
        if(data != null) {
          for(let i:number = 0 ; i < data.length ; i++) {
            if(data[i].name == this.searchInput) {
              this.temporarySearchResults.push(data[i])
            }
          }
        }
        if(this.temporarySearchResults.length == 0) {
          this.searchResultMessage = "No search results"
        } else if(this.roomType != "table") {
          this.searchRoomsForWorkspaces()
        } else if(this.roomType == "table") {
          this.searchResults = this.temporarySearchResults
        }
      })
    } else if (this.selectedCities.length > 0) {
      this.workspaceService.getWorkspacesForCities(this.selectedCities).subscribe(data => {
        if(data != null) {
          this.temporarySearchResults = data
        }
        if(this.temporarySearchResults.length == 0) {
          this.searchResultMessage = "No search results"
        } else if(this.roomType != "table") {
          this.searchRoomsForWorkspaces();
        }  else if(this.roomType == "table") {
          this.searchResults = this.temporarySearchResults
        }
    })
    } else if (this.searchInput.length > 0) {
      this.workspaceService.getWorkspace(this.searchInput).subscribe(data => {
        if(data != null) {
          this.temporarySearchResults.push(data)
        }
        if(this.temporarySearchResults.length == 0) {
          this.searchResultMessage = "No search results"
        } else if(this.roomType != "table") {
          this.searchRoomsForWorkspaces()
        }  else if(this.roomType == "table") {
          this.searchResults = this.temporarySearchResults
        }
      })
    }  
  }

  redirectToWorkspaceDetailComponent(selectedWorkspace: Workspace) {
    this.storageService.selectedWorkspace = selectedWorkspace
    this.storageService.enableReservation = true
    this.storageService.roomType = this.roomType
    if(this.roomType == "office") {
      this.storageService.officeTables = this.officeTables
    }
    this.router.navigate(["workspace-detail"])
  }
}
