import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './empty-state.html',
  styleUrl: './empty-state.css'
})
export class EmptyStateComponent {
  @Input() title = 'No data found';
  @Input() message = 'Try changing your filters or add a new scan.';
  @Input() actionLabel = '';
  @Input() actionRoute = '';
}
