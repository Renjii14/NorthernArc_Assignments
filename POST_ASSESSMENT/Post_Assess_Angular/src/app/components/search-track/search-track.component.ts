import { DatePipe } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { Track } from '../../models/track';
import { TrackService } from '../../services/track.service';
import { EmptyStateComponent } from '../shared/empty-state/empty-state.component';
import { LoadingComponent } from '../shared/loading/loading.component';
import { PageHeaderComponent } from '../shared/page-header/page-header.component';

@Component({
  selector: 'app-search-track',
  standalone: true,
  imports: [ReactiveFormsModule, DatePipe, LoadingComponent, EmptyStateComponent, PageHeaderComponent],
  templateUrl: './search-track.component.html',
  styleUrl: './search-track.component.css'
})
export class SearchTrackComponent {
  private readonly formBuilder = inject(FormBuilder);
  private readonly trackService = inject(TrackService);

  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly tracks = signal<Track[]>([]);
  readonly selectedTrack = signal<Track | null>(null);
  readonly searched = signal(false);

  readonly searchForm = this.formBuilder.nonNullable.group({
    title: ['', Validators.required]
  });

  onSearch(): void {
    if (this.searchForm.invalid) {
      this.searchForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');
    this.selectedTrack.set(null);
    this.tracks.set([]);
    this.searched.set(true);

    const title = this.searchForm.controls.title.getRawValue();

    this.trackService.searchTrack(title).subscribe({
      next: (track) => {
        this.selectedTrack.set(track);
        this.tracks.set([track]);
        this.success.set('Track found successfully.');
      },
      error: (err: Error) => {
        this.error.set(err.message);
      },
      complete: () => {
        this.loading.set(false);
      }
    });
  }

  hasTitleError(): boolean {
    const control = this.searchForm.controls.title;
    return control.touched && control.hasError('required');
  }
}
