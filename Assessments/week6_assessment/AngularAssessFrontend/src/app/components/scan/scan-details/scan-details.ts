import { Component, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ScanService } from '../../../core/services/scan.service';
import { Scan } from '../../../models/scan';
import { LoadingComponent } from '../../shared/loading/loading';

@Component({
  selector: 'app-scan-details',
  standalone: true,
  imports: [RouterLink, LoadingComponent],
  templateUrl: './scan-details.html',
  styleUrl: './scan-details.css'
})
export class ScanDetailsComponent implements OnInit {
  readonly scan = signal<Scan | null>(null);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');

  constructor(
    private readonly route: ActivatedRoute,
    private readonly scanService: ScanService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = Number(idParam);

    if (!idParam || Number.isNaN(id)) {
      this.error.set('Invalid scan ID.');
      return;
    }

    this.fetchScan(id);
  }

  fetchScan(id: number): void {
    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.scanService.getScanById(id).subscribe({
      next: (scan) => {
        this.scan.set(scan);
        this.success.set('Scan details loaded.');
        this.loading.set(false);
      },
      error: (err: Error) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}
