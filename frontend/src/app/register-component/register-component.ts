import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { PictureService } from '../services/picture-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-component',
  imports: [FormsModule],
  templateUrl: './register-component.html',
  styleUrl: './register-component.css',
})
export class RegisterComponent {

  user: User = new User()
  pictureService = inject(PictureService)
  userService = inject(UserService)
  router = inject(Router)
  message = ""

  memberRegister() {
    this.user.type = "member"
  }

  managerRegister() {
    this.user.type = "manager"
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

  register() {
    if(this.userService.validatePassword(this.user.password)) {
      this.message = "The password must have a minimum of 8 characters, a maximum of 12 characters, including at least one uppercase letter, one number and one special character, and must start with a letter."
    } else if(this.user.type == "manager" && this.user.taxIdentificationNumber.length != 9) {
      this.message = "The tax identification number must have exactly 9 digits."
    } else if(this.user.type == "manager" && this.user.taxIdentificationNumber.charAt(0) != "0") {
      this.message = "The tax identification number must start with digit 0."
    } else if(this.user.type == "manager" &&  this.user.companyRegistrationNumber.length != 8) {
      this.message = "The registration number of the company must have exactly 8 digits."
    }
    else {
      this.user.pending = true;
      if(this.selectedFile == null) {
        this.user.idPicture = -1
      }
      this.userService.postUser(this.user).subscribe(res => {
      if (res == 0 && this.user.type == "manager"){
        this.message = "The maximum number of managers is registered in the entered company."
      } else if (res == -2) {
        alert("problem sa bekom, postUser")
      } else {
        if(this.user.idPicture != -1) {
          this.pictureService.postPicture(this.selectedFile, res).subscribe(data => {
            alert("Your registration is waiting for the admin to approve it")
            this.router.navigate(["login"])
        })
        } else {
           alert("Your registration is waiting for the admin to approve it")
           this.router.navigate(["login"])
        }
      }
    })
  }}

}
