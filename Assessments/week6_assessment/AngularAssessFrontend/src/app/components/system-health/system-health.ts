import { Component, OnInit, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HealthService } from '../../core/services/health.service';
import { LoadingComponent } from '../shared/loading/loading';

@Component({
  selector: 'app-system-health',
  standalone: true,
  imports: [DatePipe, LoadingComponent],
  templateUrl: './system-health.html',
  styleUrl: './system-health.css'
})
export class SystemHealthComponent implements OnInit {
  readonly loading = signal(false);
  readonly backendStatus = signal<'online' | 'offline' | null>(null);
  readonly readyStatus = signal<'ready' | 'not-ready' | null>(null);
  readonly errorMessage = signal('');
  readonly lastCheckedTime = signal<Date | null>(null);

  constructor(private readonly healthService: HealthService) {}

  ngOnInit(): void {
    this.checkStatus();
  }

  checkStatus(): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.backendStatus.set(null);
    this.readyStatus.set(null);

    let healthCompleted = false;
    let readyCompleted = false;

    const finalize = () => {
      if (healthCompleted && readyCompleted) {
        this.lastCheckedTime.set(new Date());
        this.loading.set(false);
      }
    };

    this.healthService.checkHealth().subscribe({
      next: () => {
        this.backendStatus.set('online');
        healthCompleted = true;
        finalize();
      },
      error: (err: Error) => {
        this.backendStatus.set('offline');
        this.errorMessage.set(err.message || 'The backend service is currently unavailable.');
        healthCompleted = true;
        finalize();
      }
    });

    this.healthService.checkReady().subscribe({
      next: () => {
        this.readyStatus.set('ready');
        readyCompleted = true;
        finalize();
      },
      error: (err: Error) => {
        this.readyStatus.set('not-ready');
        if (!this.errorMessage()) {
          this.errorMessage.set(err.message || 'The backend service is currently unavailable.');
        }
        readyCompleted = true;
        finalize();
      }
    });
  }

  refresh(): void {
    this.checkStatus();
  }
}
