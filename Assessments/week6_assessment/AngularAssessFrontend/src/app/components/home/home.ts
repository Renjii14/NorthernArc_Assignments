import { Component, OnInit, computed, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ScanService } from '../../core/services/scan.service';
import { Scan } from '../../models/scan';
import { LoadingComponent } from '../shared/loading/loading';
import { EmptyStateComponent } from '../shared/empty-state/empty-state';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, LoadingComponent, EmptyStateComponent],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent implements OnInit {
  readonly scans = signal<Scan[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');

  readonly totalScans = computed(() => this.scans().length);
  readonly recentScans = computed(() =>
    [...this.scans()]
      .sort((a, b) => (b.id ?? 0) - (a.id ?? 0))
      .slice(0, 5)
  );

  constructor(private readonly scanService: ScanService) {}

  ngOnInit(): void {
    this.loadScans();
  }

  loadScans(): void {
    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.scanService.getAllScans().subscribe({
      next: (scans) => {
        this.scans.set(scans);
        this.success.set('Dashboard updated successfully.');
        this.loading.set(false);
      },
      error: (err: Error) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}
