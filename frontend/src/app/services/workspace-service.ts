import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Workspace } from '../models/workspace';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root',
})
export class WorkspaceService {
  
  http = inject(HttpClient)

  url = "http://localhost:8080/workspaces"

  getWorkspace(name: string) {
    return this.http.post<Workspace>(this.url + "/getWorkspace", name)
  }

  getWorkspacesForCities(cities: string[]) {
    return this.http.post<Workspace[]>(this.url + "/getWorkspacesForCities", cities)
  }

  getNumOfWorkspaces() {
    return this.http.get<number>(this.url + "/getNumOfWorkspaces")
  }

  getCities() {
    return this.http.get<string[]>(this.url + "/getCities")
  }

  getWorkspacesForManager(user: User) {
    return this.http.post<Workspace[]>(this.url + "/getWorkspacesForManager", user)
  }

  postWorkspace(workspace: Workspace) {
    return this.http.post<number>(this.url + "/postWorkspace", workspace)
  }

  updateWorkspace(workspace: Workspace) {
    return this.http.post<number>(this.url + "/updateWorkspace", workspace)
  }

  getAllWorkspaces() {
    return this.http.get<Workspace[]>(this.url + "/getAllWorkspaces")
  }

  deleteWorkspace(workspace: Workspace) {
    return this.http.post<number>(this.url + "/deleteWorkspace", workspace)
  }
}
