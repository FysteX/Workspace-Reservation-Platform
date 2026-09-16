import { Component, inject, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';

/*
npm install @fullcalendar/angular
npm install @fullcalendar/daygrid
npm install @fullcalendar/timegrid
npm install @fullcalendar/interaction
ng add @angular/material
npm install jspdf
*/


@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title="New app"

  router = inject(Router);

  noPath: boolean = false;

  ngOnInit() {
    this.login()
  }

  login() {
    this.router.navigate(["/login"]); 
  }
  
}
