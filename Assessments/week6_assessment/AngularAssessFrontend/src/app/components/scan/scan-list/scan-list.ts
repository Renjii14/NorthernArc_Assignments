import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ScanService } from '../../../core/services/scan.service';
import { Scan } from '../../../models/scan';
import { LoadingComponent } from '../../shared/loading/loading';
import { EmptyStateComponent } from '../../shared/empty-state/empty-state';

@Component({
  selector: 'app-scan-list',
  standalone: true,
  imports: [RouterLink, LoadingComponent, EmptyStateComponent],
  templateUrl: './scan-list.html',
  styleUrl: './scan-list.css'
})
export class ScanListComponent implements OnInit {
  readonly scans = signal<Scan[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');

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
        this.loading.set(false);
      },
      error: (err: Error) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }

  deleteScan(scan: Scan): void {
    const confirmed = window.confirm(
      `Delete scan ${scan.id} for ${scan.domainName}? This performs a soft delete.`
    );

    if (!confirmed || !scan.id) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.scanService.deleteScan(scan.id).subscribe({
      next: () => {
        this.success.set('Scan deleted successfully.');
        this.loadScans();
      },
      error: (err: Error) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}
