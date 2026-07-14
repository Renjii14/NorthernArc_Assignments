import { Component, inject, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthStateService } from '../../core/auth/auth-state.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.css'
})
export class EditProfileComponent {
  private readonly authState = inject(AuthStateService);
  private readonly router = inject(Router);

  readonly currentUser = computed(() => this.authState.currentUser());
  
  name = signal('');
  email = signal('');
  customerId = signal('');
  
  isLoading = signal(false);
  message = signal('');
  messageType = signal<'success' | 'error'>('success');

  constructor() {
    const user = this.currentUser();
    if (user) {
      this.name.set(user.name || '');
      this.email.set(user.email || '');
      this.customerId.set(user.customerId?.toString() || '');
    }
  }

  onSave() {
    this.isLoading.set(true);
    this.message.set('');

    // TODO: Call the update profile API
    // For now, just show success and navigate back
    setTimeout(() => {
      this.isLoading.set(false);
      this.message.set('Profile updated successfully!');
      this.messageType.set('success');
      setTimeout(() => {
        this.router.navigate(['/profile']);
      }, 1500);
    }, 1000);
  }

  onCancel() {
    this.router.navigate(['/profile']);
  }
}
