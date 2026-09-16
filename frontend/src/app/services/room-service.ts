import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Workspace } from '../models/workspace';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root',
})
export class RoomService {
  
  http = inject(HttpClient)

  url = "http://localhost:8080/rooms"

  getRoomsForWorkspaces(workspaces: Workspace[]) {
    return this.http.post<Room[]>(this.url + "/getRoomsForWorkspaces", workspaces)
  }

  postRoom(room: Room) {
    return this.http.post<number>(this.url + "/postRoom", room)
  }

  updateRoom(room: Room) {
    return this.http.post<number>(this.url + "/updateRoom", room)
  }

}
