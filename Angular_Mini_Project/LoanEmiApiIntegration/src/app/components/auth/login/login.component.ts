import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService, LoginRequest } from '../../../services/auth/auth.service';
import { TokenStorageService } from '../../../services/auth/token-storage.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']})
export class LoginComponent implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly tokenStorage = inject(TokenStorageService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  loginDto: LoginRequest = {
    email: '',
    password: ''
  };

  readonly status = signal({
    loading: false,
    success: false,
    error: '',
    message: ''
  });

  ngOnInit(): void {
    const registered = this.route.snapshot.queryParamMap.get('registered') === '1';
    const email = this.route.snapshot.queryParamMap.get('email') || '';
    if (registered) {
      this.status.set({
        loading: false,
        success: true,
        error: '',
        message: 'Registration completed. Please login with your new account.'
      });
      if (email) {
        this.loginDto.email = email;
      }
    }

    if (this.tokenStorage.isLoggedIn()) {
      this.router.navigateByUrl(this.getDefaultRouteForRole());
    }
  }

  submit(): void {
    const validationError = this.validateForm();
    if (validationError) {
      this.status.set({ loading: false, success: false, error: validationError, message: '' });
      return;
    }

    this.status.set({ loading: true, success: false, error: '', message: '' });

    this.authService.login(this.loginDto).subscribe({
      next: (response) => {
        this.tokenStorage.saveToken(response.token);
        const defaultRoute = this.getDefaultRouteForRole();
        this.status.set({
          loading: false,
          success: true,
          error: '',
          message: 'Login successful. Redirecting...'
        });
        this.reset();
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl') || defaultRoute;
        this.router.navigateByUrl(returnUrl);
      },
      error: (error) => {
        this.status.set({
          loading: false,
          success: false,
          error: error?.error?.message || error?.message || 'Something went wrong.',
          message: ''
        });
      }
    });
  }

  reset(): void {
    this.loginDto = {
      email: '',
      password: ''
    };
  }

  private validateForm(): string {
    if (!this.loginDto.email || !this.loginDto.password) {
      return 'Email and password are required.';
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(this.loginDto.email)) {
      return 'Please enter a valid email address.';
    }

    return '';
  }

  private getDefaultRouteForRole(): string {
    return '/dashboard';
  }
}

