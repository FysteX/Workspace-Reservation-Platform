import { Component, inject } from '@angular/core';
import { StorageService } from '../services/storage-service';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reset-password-link-component',
  imports: [FormsModule],
  templateUrl: './reset-password-link-component.html',
  styleUrl: './reset-password-link-component.css',
})
export class ResetPasswordLinkComponent {

  storageService = inject(StorageService)
  userService = inject(UserService)
  router = inject(Router)

  user = this.storageService.resetPasswordUser
  password = ""
  message = ""

  ngOnInit() {
    if (new Date().getTime() - this.storageService.resetPasswordLinkStartTime.getTime() > 10 * 1000 * 60 * 30) {
      alert("Vreme linka je isteklo")
      this.router.navigate(["login"])
    }
  }

  changePassword() {
    this.message = ""
    if(this.userService.validatePassword(this.password)) {
      this.message = "The password must have a minimum of 8 characters, a maximum of 12 characters, including at least one uppercase letter, one number and one special character, and must start with a letter."
    } else {
      this.user.password = this.password;
      this.userService.changePassword(this.user).subscribe(data => {
        alert("Password changed succesfully")
        this.router.navigate(["login"])
      })
    }
    
  }

}
