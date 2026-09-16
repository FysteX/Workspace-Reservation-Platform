import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  http = inject(HttpClient);

  url = "http://localhost:8080/users";

  ///vraca true ako je sifra neispravna
  validatePassword(password: string): boolean {  
    const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[a-zA-Z].{7,11}$/;
    return !regex.test(password);
  }

  getUser(user: User) {
    const data = {
      username : "",
    password: "",
    firstname: "",
    lastname: "",
    number: "",
    email: "",
    firmName: "",
    firmAdress: "",
    companyRegistrationNumber: "",
    taxIdentificationNumber: "",
    type: "",
    pending: true,
    idPicture: 0
    }

    return this.http.post<User>(this.url + "/getUser", data);
  }

  postUser(user: User) {
    return this.http.post<number>(this.url + "/postUser", user);
  }

  getPendingUsers() {
    return this.http.get<User[]>(this.url + "/getPendingUsers");
  }

  updateUser(user: User) {
    return this.http.post<number>(this.url + "/updateUser", user);
  }

  getAllUsers() {
    return this.http.get<User[]>(this.url + "/getAllUsers")
  }

  deleteUser(user: User) {
    return this.http.post<number>(this.url + "/deleteUser", user)
  }

  getUserByEmail(user: User) {
    return this.http.post<User>(this.url + "/getUserByEmail", user);
  }

  getUserByUsername(user: User) {
    return this.http.post<User>(this.url + "/getUserByUsername", user);
  }

  changePassword(user: User) {
    return this.http.post<number>(this.url + "/changePassword", user);
  }
}
