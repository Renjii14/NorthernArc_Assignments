import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService, RegisterRequest } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  registerDto: RegisterRequest = {
    customerName: '',
    email: '',
    password: '',
    phoneNumber: '',
    city: ''
  };

  confirmPassword = '';
  submitAttempted = false;

  readonly status = signal({
    loading: false,
    success: false,
    error: '',
    message: ''
  });

  submit(): void {
    this.submitAttempted = true;

    const validationError = this.validateForm();
    if (validationError) {
      this.status.set({ loading: false, success: false, error: validationError, message: '' });
      return;
    }

    this.status.set({ loading: true, success: false, error: '', message: '' });

    this.authService.register(this.registerDto).subscribe({
      next: () => {
        this.status.set({
          loading: false,
          success: true,
          error: '',
          message: 'Registration successful. Please login to continue.'
        });
        this.router.navigate(['/login'], {
          queryParams: {
            registered: '1',
            email: this.registerDto.email
          }
        });
      },
      error: (error) => {
        this.status.set({
          loading: false,
          success: false,
          error: error?.error?.message || error?.message || 'Unable to complete registration.',
          message: ''
        });
      }
    });
  }

  hasMinLength(): boolean {
    return this.registerDto.password.length >= 8;
  }

  hasUppercase(): boolean {
    return /[A-Z]/.test(this.registerDto.password);
  }

  hasLowercase(): boolean {
    return /[a-z]/.test(this.registerDto.password);
  }

  hasDigit(): boolean {
    return /\d/.test(this.registerDto.password);
  }

  hasSpecialCharacter(): boolean {
    return /[^A-Za-z0-9]/.test(this.registerDto.password);
  }

  isStrongPassword(): boolean {
    return this.hasMinLength() && this.hasUppercase() && this.hasLowercase() && this.hasDigit() && this.hasSpecialCharacter();
  }

  showPasswordValidation(): boolean {
    return this.submitAttempted || this.registerDto.password.length > 0;
  }

  hasConfirmPasswordMismatch(): boolean {
    if (!this.confirmPassword) {
      return false;
    }
    return this.registerDto.password !== this.confirmPassword;
  }

  showConfirmPasswordRequiredError(): boolean {
    return this.submitAttempted && !this.confirmPassword;
  }

  private validateForm(): string {
    if (
      !this.registerDto.customerName ||
      !this.registerDto.email ||
      !this.registerDto.phoneNumber ||
      !this.registerDto.city ||
      !this.registerDto.password ||
      !this.confirmPassword
    ) {
      return 'All fields are required.';
    }

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(this.registerDto.email)) {
      return 'Please enter a valid email address.';
    }

    const phonePattern = /^[0-9]{10}$/;
    if (!phonePattern.test(this.registerDto.phoneNumber)) {
      return 'Phone number must be 10 digits.';
    }

    if (!this.isStrongPassword()) {
      return 'Password does not meet required constraints.';
    }

    if (this.registerDto.password !== this.confirmPassword) {
      return 'Password and confirm password do not match.';
    }

    return '';
  }
}
