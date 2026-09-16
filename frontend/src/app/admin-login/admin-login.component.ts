import { Component, inject } from '@angular/core';
import { UserService } from '../services/user.service';
import { FormsModule } from '@angular/forms';
import { User } from '../models/user';
import { StorageService } from '../services/storage-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.css'
})
export class AdminLoginComponent {

  user: User = new User()
  pendingUsers: User[] = []

  message = ""

  userService = inject(UserService)
  storageService = inject(StorageService)
  router = inject(Router)

  login() {
    this.message = ""
    this.user.type = "admin"
    this.userService.getUser(this.user).subscribe(data => {
      if(data != null){
        if(data.type == "admin") {
          this.storageService.loggedinUser = data
          this.router.navigate(["admin"])
        } else {
          this.message = "Incorrect username or password"
        }
      }
    })
  }

}
