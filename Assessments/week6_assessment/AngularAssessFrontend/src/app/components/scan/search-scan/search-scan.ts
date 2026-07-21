import { Component, signal } from '@angular/core';
import { DecimalPipe, LowerCasePipe } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ScanService } from '../../../core/services/scan.service';
import { Scan } from '../../../models/scan';
import { LoadingComponent } from '../../shared/loading/loading';
import { EmptyStateComponent } from '../../shared/empty-state/empty-state';

@Component({
  selector: 'app-search-scan',
  standalone: true,
  imports: [ReactiveFormsModule, DecimalPipe, LowerCasePipe, LoadingComponent, EmptyStateComponent],
  templateUrl: './search-scan.html',
  styleUrl: './search-scan.css'
})
export class SearchScanComponent {
  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly results = signal<Scan[]>([]);
  readonly searched = signal(false);

  readonly orderByOptions = [
    'id',
    'domainName',
    'numPages',
    'numBrokenLinks',
    'numMissingImages',
    'deleted'
  ];

  readonly form;
  readonly idForm;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly scanService: ScanService
  ) {
    this.form = this.formBuilder.group({
      domainName: ['', [Validators.required, Validators.minLength(2)]],
      orderBy: ['id', [Validators.required]]
    });

    this.idForm = this.formBuilder.group({
      scanId: ['', [Validators.required, Validators.pattern(/^\d+$/)]]
    });
  }

  searchById(): void {
    if (this.idForm.invalid) {
      this.idForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');
    this.results.set([]);
    this.searched.set(true);

    const id = Number(this.idForm.getRawValue().scanId);

    this.scanService.getScanById(id).subscribe({
      next: (scan) => {
        if (scan && !scan.deleted) {
          this.results.set([scan]);
          this.success.set('Scan found.');
        } else {
          this.results.set([]);
          this.error.set('Scan not found or has been deleted.');
        }
        this.loading.set(false);
      },
      error: (err: Error) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }

  search(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');
    this.results.set([]);
    this.searched.set(true);

    const value = this.form.getRawValue();

    this.scanService.searchScans(String(value.domainName).trim(), String(value.orderBy)).subscribe({
      next: (results) => {
        this.results.set(results.filter((item) => !item.deleted));
        this.success.set('Search completed.');
        this.loading.set(false);
      },
      error: (err: Error) => {
        this.error.set(err.message);
        this.loading.set(false);
      }
    });
  }
}
