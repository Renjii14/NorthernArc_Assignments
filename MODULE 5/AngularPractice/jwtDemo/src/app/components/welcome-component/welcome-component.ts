import { Component, inject, OnInit } from '@angular/core';
import { LoginService } from '../../services/login-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-welcome-component',
  imports: [],
  templateUrl: './welcome-component.html',
  styleUrl: './welcome-component.css',
})
export class WelcomeComponent implements OnInit{
  loginService:LoginService =inject(LoginService);
  router:Router =inject(Router);
ngOnInit(): void {
  if(localStorage.getItem("token")==null)
    this.router.navigate(["/login"]);
}
}
