import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { CustomerResponse } from '../../services/customer/customer.service';
import { PageHeaderComponent } from '../shared/page-header/page-header.component';
import { ProfileCardComponent } from '../shared/profile-card/profile-card.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [FormsModule, PageHeaderComponent, ProfileCardComponent],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
})
export class ProfileComponent implements OnInit, OnDestroy {
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private redirectTimer: ReturnType<typeof setTimeout> | null = null;

  readonly profile = signal<CustomerResponse | null>(null);
  readonly status = signal({ loading: false, error: '' });
  readonly section = signal<'profile' | 'account' | 'password'>('profile');
  readonly passwordState = signal({ loading: false, error: '', success: '' });

  currentPassword = '';
  newPassword = '';
  confirmPassword = '';

  ngOnInit(): void {
    this.route.queryParamMap.subscribe((params) => {
      const rawSection = params.get('section');
      if (rawSection === 'account' || rawSection === 'password') {
        this.section.set(rawSection);
        return;
      }
      this.section.set('profile');
    });

    this.loadData();
  }

  ngOnDestroy(): void {
    if (this.redirectTimer) {
      clearTimeout(this.redirectTimer);
      this.redirectTimer = null;
    }
  }

  sectionTitle(): string {
    if (this.section() === 'account') {
      return 'My Account';
    }
    if (this.section() === 'password') {
      return 'Change Password';
    }
    return 'Profile Overview';
  }

  sectionDescription(): string {
    if (this.section() === 'account') {
      return 'View your account identity, role, and communication profile details.';
    }
    if (this.section() === 'password') {
      return 'Password update flow is managed through secured authentication services.';
    }
    return 'Review your profile information and current account details.';
  }

  submitPasswordChange(): void {
    this.passwordState.set({ loading: false, error: '', success: '' });

    const currentPassword = this.currentPassword.trim();
    const newPassword = this.newPassword.trim();
    const confirmPassword = this.confirmPassword.trim();

    if (!currentPassword || !newPassword || !confirmPassword) {
      this.passwordState.set({ loading: false, error: 'Please fill all password fields.', success: '' });
      return;
    }

    if (newPassword !== confirmPassword) {
      this.passwordState.set({ loading: false, error: 'New password and confirm password must match.', success: '' });
      return;
    }

    if (newPassword.length < 8) {
      this.passwordState.set({ loading: false, error: 'New password must be at least 8 characters.', success: '' });
      return;
    }

    this.passwordState.set({ loading: true, error: '', success: '' });
    this.authService.changePassword({ currentPassword, newPassword }).subscribe({
      next: (response) => {
        this.passwordState.set({
          loading: false,
          error: '',
          success: response?.message || 'Password changed successfully.'
        });
        this.currentPassword = '';
        this.newPassword = '';
        this.confirmPassword = '';

        if (this.redirectTimer) {
          clearTimeout(this.redirectTimer);
        }
        this.redirectTimer = setTimeout(() => {
          this.router.navigate([], {
            relativeTo: this.route,
            queryParams: { section: 'account' },
            queryParamsHandling: 'merge'
          });
        }, 1500);
      },
      error: (error) => {
        this.passwordState.set({
          loading: false,
          error: error?.error?.message || error?.message || 'Failed to change password.',
          success: ''
        });
      }
    });
  }

  loadData(): void {
    this.status.set({ loading: true, error: '' });
    this.authService.getCurrentUser().subscribe({
      next: (response) => {
        this.profile.set(response);
        this.status.set({ loading: false, error: '' });
      },
      error: (error) => {
        this.status.set({
          loading: false,
          error: error?.error?.message || error?.message || 'Something went wrong.'
        });
      }
    });
  }
}

