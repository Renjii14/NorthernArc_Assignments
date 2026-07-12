import { Component, inject } from '@angular/core';
import { CountServices } from '../../services/count-service';

@Component({
  selector: 'app-increment-count',
  imports: [],
  templateUrl: './increment-count.html',
  styleUrl: './increment-count.css',
})
export class IncrementCount {
  countService:CountServices=inject(CountServices);
}
