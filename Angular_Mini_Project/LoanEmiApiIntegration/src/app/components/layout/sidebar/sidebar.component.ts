import { CommonModule } from '@angular/common';
import { Component, Input, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { TokenStorageService } from '../../../services/auth/token-storage.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']})
export class SidebarComponent {
  private readonly tokenStorage = inject(TokenStorageService);

  @Input() collapsed = false;

  canAccessDashboard(): boolean {
    return this.tokenStorage.isLoggedIn();
  }

  canAccessCustomers(): boolean {
    return this.tokenStorage.hasRole(['MANAGER', 'ADMIN']);
  }

  canManageInterest(): boolean {
    return this.tokenStorage.hasRole(['MANAGER', 'ADMIN']);
  }

  canAccessMyRequests(): boolean {
    return this.tokenStorage.hasRole(['USER']);
  }

  canAccessEmiPayments(): boolean {
    return this.tokenStorage.hasRole(['USER']);
  }

  canAccessReports(): boolean {
    return this.tokenStorage.hasRole(['MANAGER', 'ADMIN']);
  }
}

