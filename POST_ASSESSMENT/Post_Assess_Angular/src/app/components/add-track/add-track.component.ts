import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';

import { Track } from '../../models/track';
import { TrackRequest } from '../../models/track-request';
import { TrackService } from '../../services/track.service';
import { PageHeaderComponent } from '../shared/page-header/page-header.component';

@Component({
  selector: 'app-add-track',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink, PageHeaderComponent],
  templateUrl: './add-track.component.html',
  styleUrl: './add-track.component.css'
})
export class AddTrackComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly trackService = inject(TrackService);
  private readonly router = inject(Router);

  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly tracks = signal<Track[]>([]);
  readonly selectedTrack = signal<Track | null>(null);

  readonly trackForm = this.formBuilder.nonNullable.group({
    title: ['', Validators.required],
    albumName: ['', Validators.required],
    releaseDate: ['', Validators.required],
    playCount: [0, [Validators.required, Validators.min(0)]]
  });

  onSubmit(): void {
    if (this.trackForm.invalid) {
      this.trackForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    const formValue = this.trackForm.getRawValue();

    const trackRequest: TrackRequest = {
      title: formValue.title,
      albumName: formValue.albumName,
      releaseDate: new Date(formValue.releaseDate).toISOString(),
      playCount: formValue.playCount
    };

    this.trackService.createTrack(trackRequest).subscribe({
      next: (track) => {
        this.selectedTrack.set(track);
        this.success.set('Track created successfully. Redirecting to track list...');
      },
      error: (err: Error) => {
        this.error.set(err.message);
      },
      complete: () => {
        this.loading.set(false);
        if (this.selectedTrack()) {
          setTimeout(() => {
            this.router.navigate(['/tracks']);
          }, 900);
        }
      }
    });
  }

  hasError(controlName: 'title' | 'albumName' | 'releaseDate' | 'playCount', errorName: string): boolean {
    const control = this.trackForm.controls[controlName];
    return control.touched && control.hasError(errorName);
  }
}
