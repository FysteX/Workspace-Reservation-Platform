import { Component, inject } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { FormsModule } from '@angular/forms';
import { WorkspaceService } from '../services/workspace-service';
import { Workspace } from '../models/workspace';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-component',
  imports: [FormsModule],
  templateUrl: './admin-component.html',
  styleUrl: './admin-component.css',
})
export class AdminComponent {

  userService = inject(UserService)
  workspaceService = inject(WorkspaceService)
  router = inject(Router)

  //users variables
  users: User[] = []
  userBeingUpdated = new User()
  updating = false
  updatingMessage = ""

  //workspaces variables
  workspaces: Workspace[] = []

  ngOnInit() {
    this.getAllUsers()
    this.getAllWorkspaces()
  }

  getAllWorkspaces() {
    this.workspaceService.getAllWorkspaces().subscribe(data =>{
      if(data) {
        this.workspaces = data
      } else {
        alert("Problem sa bekom, getAllWorkspaces")
      }
    })
  }

  getAllUsers() {
    this.userService.getAllUsers().subscribe(data => {
      if(data) {
        this.users = data
      } else {
        alert("Problem sa bekom, getAllUsers")
      }
    })
  }

  register(user: User) {
    this.updating = false
    user.pending = false
    this.userService.updateUser(user).subscribe(data => {
      if(data > 0) {
        this.getAllUsers()
      } else if(data < 0) {
        alert("Problem sa bekom, updateUser")
      }
    })
  }

  update(user: User) {
    this.updating = true
    this.userBeingUpdated = user    
  }

  delete(user: User) {
    this.updating = false
    this.userService.deleteUser(user).subscribe(data => {
      if(data > 0) {
        this.getAllUsers()
      } else if(data < 0){
        alert("Problem sa bekom, deleteUser")
      }
    })
  }

  changeUserData() {
    if(this.userService.validatePassword(this.userBeingUpdated.password)) {
      this.updatingMessage = "The password must have a minimum of 8 characters, a maximum of 12 characters, including at least one uppercase letter, one number and one special character, and must start with a letter."
    } else if(this.userBeingUpdated.type == "manager" && this.userBeingUpdated.taxIdentificationNumber.length != 9) {
      this.updatingMessage = "The tax identification number must have exactly 9 digits."
    } else if(this.userBeingUpdated.type == "manager" && this.userBeingUpdated.taxIdentificationNumber.charAt(0) != "0") {
      this.updatingMessage = "The tax identification number must start with digit 0."
    } else if(this.userBeingUpdated.type == "manager" &&  this.userBeingUpdated.companyRegistrationNumber.length != 8) {
      this.updatingMessage = "The registration number of the company must have exactly 8 digits."
    } else {
      this.userService.updateUser(this.userBeingUpdated).subscribe(data => {
        if(!data) {
          alert("Problem sa bekom, updateUser")
        } else {
          this.getAllUsers
        }
      })
    }
  }

  approveWorkspace(workspace: Workspace) {
    workspace.activeStatus = true
    this.workspaceService.updateWorkspace(workspace).subscribe(data => {
      if(data < 0) {
        alert("Problem sa bekom, updateWorkspace")
      } else if(data > 0) {
        this.getAllWorkspaces()
      }
    })
  }

  logout() {
    this.router.navigate(["login"])
  }

  disapproveWorkspace(workspace: Workspace) {
    this.workspaceService.deleteWorkspace(workspace).subscribe(data => {
      if(data > 0) {
        this.getAllWorkspaces()
      } else if (data < 0) {
        alert("Problem sa bekom, deleteWorkspace")
      }
    })
  }

}
