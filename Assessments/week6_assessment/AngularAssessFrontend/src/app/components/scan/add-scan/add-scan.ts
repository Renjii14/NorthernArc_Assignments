import { Component, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { ScanService } from '../../../core/services/scan.service';
import { LoadingComponent } from '../../shared/loading/loading';

@Component({
  selector: 'app-add-scan',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, LoadingComponent],
  templateUrl: './add-scan.html',
  styleUrl: './add-scan.css'
})

export class AddScanComponent {
  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly form;

  constructor(
    private readonly formBuilder: FormBuilder,
    private readonly scanService: ScanService,
    private readonly router: Router
  ) {
    this.form = this.formBuilder.group({
      domainName: ['', [Validators.required, Validators.minLength(2)]],
      numPages: [0, [Validators.required, Validators.min(0)]],
      numBrokenLinks: [0, [Validators.required, Validators.min(0)]],
      numMissingImages: [0, [Validators.required, Validators.min(0)]]
    });
  }

  submit(): void {
    if (this.form.invalid || this.loading()) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    const value = this.form.getRawValue();

    this.scanService
      .createScan({
        domainName: String(value.domainName).trim(),
        numPages: Number(value.numPages),
        numBrokenLinks: Number(value.numBrokenLinks),
        numMissingImages: Number(value.numMissingImages)
      })
      .subscribe({
        next: () => {
          this.success.set('Scan created successfully. Redirecting...');
          this.loading.set(false);
          void this.router.navigate(['/scans']);
        },
        error: (err: Error) => {
          this.error.set(err.message);
          this.loading.set(false);
        }
      });
  }

  hasError(field: 'domainName' | 'numPages' | 'numBrokenLinks' | 'numMissingImages'): boolean {
    const control = this.form.controls[field];
    return control.invalid && (control.dirty || control.touched);
  }
}
