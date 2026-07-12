import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastNotificationService } from '../../../../services/shared/toast-notification.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast-container.component.html',
  styleUrls: ['./toast-container.component.css']
})
export class ToastContainerComponent {
  readonly toastService = inject(ToastNotificationService);

  dismiss(id: number): void {
    this.toastService.dismiss(id);
  }
}
