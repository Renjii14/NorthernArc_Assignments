import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { LoginComponent } from "./components/login-component/login-component";
import { LoginService } from './services/login-service';

@Component({
  selector: 'app-root',
  imports: [ RouterLink, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App{
  //loginService:LoginService = inject(LoginService);
  token:WritableSignal<string | null> = signal(null);

  checkToken(){
    return localStorage.getItem("token");
  }
  
}
