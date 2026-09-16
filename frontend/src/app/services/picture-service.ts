import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PictureService {
  
  http = inject(HttpClient)

  url = "http://localhost:8080/pictures";

  postPicture(file: File, id: number) {
    const formData = new FormData()
    formData.append("file", file)
    if(id == -1) {
      formData.append("id", "deffault")
    } else {
      formData.append("id", id.toString())
    }
    return this.http.post<number>(this.url + "/postPicture", formData)
  }

  getPicture(id: string): Observable<Blob> {
    return this.http.post<Blob>(this.url + "/getPicture", id, { responseType: 'blob' as 'json' })
  }
}
