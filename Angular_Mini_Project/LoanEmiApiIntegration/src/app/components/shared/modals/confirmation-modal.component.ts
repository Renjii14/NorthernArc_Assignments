import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-confirmation-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css']})
export class ConfirmationModalComponent {
  @Input() visible = false;
  @Input() title = 'Confirm Action';
  @Input() message = '';
}

