import { Component, inject } from '@angular/core';
import { CountServices } from '../../services/count-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-decrement-by',
  imports: [FormsModule],
  templateUrl: './decrement-by.html',
  styleUrl: './decrement-by.css',
})
export class DecrementBy {
  countService:CountServices=inject(CountServices);
  decrementValue: number = 0;

  decrementCount() {
    this.countService.decrementBy(this.decrementValue);
    this.decrementValue = 0;
  }
}
