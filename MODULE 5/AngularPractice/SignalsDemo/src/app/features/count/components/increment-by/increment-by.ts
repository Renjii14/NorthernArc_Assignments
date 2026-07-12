import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CountServices } from '../../services/count-service';

@Component({
  selector: 'app-increment-by',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './increment-by.html',
  styleUrl: './increment-by.css',
})
export class IncrementBy {

  countService = inject(CountServices);

  incrementValue: number = 0;

  incrementCount() {
    this.countService.incrementBy(this.incrementValue);
    this.incrementValue = 0;
  }

}