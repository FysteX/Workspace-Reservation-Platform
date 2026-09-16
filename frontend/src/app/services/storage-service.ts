import { Injectable } from '@angular/core';
import { Workspace } from '../models/workspace';
import { User } from '../models/user';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  
  selectedWorkspace: Workspace = new Workspace()

  loggedinUser: User = new User()

  resetPasswordUser: User = new User()
  resetPasswordLinkStartTime: Date = new Date()

  enableReservation = false
  roomType = ""
  officeTables = 0

}
