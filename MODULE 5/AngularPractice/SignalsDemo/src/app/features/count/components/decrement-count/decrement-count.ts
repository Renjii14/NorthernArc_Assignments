import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CountServices } from '../../services/count-service';

@Component({
  selector: 'app-decrement-count',
  imports: [FormsModule],
  templateUrl: './decrement-count.html',
  styleUrl: './decrement-count.css',
})
export class DecrementCount {
  decrementValue: number = 0;
  countService:CountServices=inject(CountServices);
  DecrementCount() {
    this.countService.decrementBy(this.decrementValue);
    this.decrementValue = 0;
  }
}
