import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { StorageService } from '../services/storage-service';

@Component({
  selector: 'app-reset-password-component',
  imports: [FormsModule, RouterLink],
  templateUrl: './reset-password-component.html',
  styleUrl: './reset-password-component.css',
})
export class ResetPasswordComponent {

  selectedOption = -1
  user = new User()
  foundUser = new User()
  message = ""
  submited = false

  userService = inject(UserService)
  storageService = inject(StorageService)


  selectOption(option: number) {
    this.selectedOption = option;
  }

  usernameSubmit() {
    this.message = ""
    this.userService.getUserByUsername(this.user).subscribe(data => {
      if(data) {
        this.foundUser = data
        this.submited = true
        this.storageService.resetPasswordUser = data
        this.storageService.resetPasswordLinkStartTime = new Date()
      } else {
        this.message = "There is no registered user with entered username"
      }
    })
  }

  emailSubmit() {
    this.message = ""
    this.userService.getUserByEmail(this.user).subscribe(data => {
      if(data) {
        this.foundUser = data
        this.submited = true
        this.storageService.resetPasswordUser = data
        this.storageService.resetPasswordLinkStartTime = new Date()
      } else {
        this.message = "There is no registered user with entered email"
      }
    })
  }

}
