import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Required for [(ngModel)] usage
import JwtRequestDTO from '../../dto/JwtRequestDTO';
import { LoginService } from '../../services/login-service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './login-component.html',
  styles: [`
    :host {
      display: block;
      width: 100%;
    }
  `]
})
export class LoginComponent implements OnInit{
  loginService:LoginService = inject(LoginService);
  // Simple data object for two-way binding mapping to backend DTO structure
  credentials:JwtRequestDTO = {
    username: '',
    password: ''
  };

  formSubmitted = false;
  hidePassword = true;

  constructor(private router: Router) {}
  ngOnInit(): void {
   
  if(localStorage.getItem("token")!=null)
    this.router.navigate(["\welcome"]);
}
  

  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }

  onSubmit(isValid: boolean | null): void {
    this.formSubmitted = true;

    // Check HTML5 structural constraint validation before proceeding
    if (!isValid) {
      return;
    }

    // Direct payload access via model bindings
    console.log('Dispatched Action: POST /api/auth/login', this.credentials);
    this.loginService.login(this.credentials).subscribe({
      next:(data)=>{
        console.log(data);
        localStorage.setItem("token",data.token);
        //this.loginService.token.set(localStorage.getItem("token"));
        this.router.navigate(['/welcome']);
      },
      error:(err)=>{
        console.log(err);
      }
    });
    // Redirect logic goes here...
     
  }
}