import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { Router, RouterLink } from '@angular/router';
import { User } from '../models/user';
import { WorkspaceService } from '../services/workspace-service';
import { Workspace } from '../models/workspace';
import { StorageService } from '../services/storage-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  user: User = new User()

  message = ""

  adminPassword = ""
  adminPasswordMessage = ""

  numOfWorkspaces = 0
  cities: string[] = []
  selectedCities: string[] = []
  searchInput = ""
  searchResults : Workspace[] = []
  searchResultMessage = ""

  userService = inject(UserService);
  workspaceService = inject(WorkspaceService)
  storageService = inject(StorageService)
  router = inject(Router);

  search() {
    this.searchResults = []
    this.searchResultMessage = ""
    if(this.selectedCities.length > 0 && this.searchInput.length > 0) {
      this.workspaceService.getWorkspacesForCities(this.selectedCities).subscribe(data => {
        if(data != null) {
          for(let i:number = 0 ; i < data.length ; i++) {
            if(data[i].name == this.searchInput) {
              this.searchResults.push(data[i])
            }
          }
        }
        if(this.searchResults.length == 0) {
          this.searchResultMessage = "No search results"
        }
      })
    } else if (this.selectedCities.length > 0) {
      this.workspaceService.getWorkspacesForCities(this.selectedCities).subscribe(data => {
        if(data != null) {
          this.searchResults = data
        }
        if(this.searchResults.length == 0) {
          this.searchResultMessage = "No search results"
        }
    })
    } else if (this.searchInput.length > 0) {
      this.workspaceService.getWorkspace(this.searchInput).subscribe(data => {
        if(data != null) {
          this.searchResults.push(data)
        }
        if(this.searchResults.length == 0) {
          this.searchResultMessage = "No search results"
        }
      })
    }  
  }

  ngOnInit() {
    this.workspaceService.getNumOfWorkspaces().subscribe(data => {
      if(data >= 0) {
        this.numOfWorkspaces = data;
      } else {
        alert("Greska sa bekom, numOfWorkspaces")
      }
    })
    this.workspaceService.getCities().subscribe(data => {
      if (data!= null) {
        this.cities = data;
      }
    })
  }

  login() {
    this.userService.getUser(this.user).subscribe(data => {
      if(data != null){
        this.message = ""
        this.storageService.loggedinUser = data
        if(data.type == "member") {
          this.router.navigate(["member"])
        } else if(data.type = "manager") {
          this.router.navigate(["manager"])
        }
      } else {
        this.message = "wrong username or password"
      }
    })
  }

  redirectToAdminLoginComponent() {
    if(this.adminPassword == "admin123") {
      this.router.navigate(["admin-login"]);
      this.adminPasswordMessage = ""
    } else {
      this.adminPasswordMessage = "Wrong admin password"
    }
  }

  redirectToRegisterComponent() {
    this.router.navigate(["register"]);
  }

  redirectToWorkspaceDetailComponent(selectedWorkspace: Workspace) {
    this.storageService.enableReservation = false
    this.storageService.selectedWorkspace = selectedWorkspace
    this.router.navigate(["workspace-detail"])
  }

  redirectToForgotPasswordComponent() {
    this.router.navigate(["reset-password"])
  }

}
