import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Track } from '../../models/track';
import { TrackService } from '../../services/track.service';
import { EmptyStateComponent } from '../shared/empty-state/empty-state.component';
import { LoadingComponent } from '../shared/loading/loading.component';
import { PageHeaderComponent } from '../shared/page-header/page-header.component';

@Component({
  selector: 'app-track-list',
  standalone: true,
  imports: [RouterLink, DatePipe, LoadingComponent, EmptyStateComponent, PageHeaderComponent],
  templateUrl: './track-list.component.html',
  styleUrl: './track-list.component.css'
})
export class TrackListComponent implements OnInit {
  private readonly trackService = inject(TrackService);

  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly tracks = signal<Track[]>([]);
  readonly selectedTrack = signal<Track | null>(null);

  ngOnInit(): void {
    this.loadTracks();
  }

  loadTracks(): void {
    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.trackService.getAllTracks().subscribe({
      next: (tracks) => {
        this.tracks.set(tracks);
      },
      error: (err: Error) => {
        this.error.set(err.message);
      },
      complete: () => {
        this.loading.set(false);
      }
    });
  }

  onDelete(track: Track): void {
    if (!track.id) {
      this.error.set('Track ID is missing and cannot be deleted.');
      return;
    }

    const confirmed = window.confirm(`Delete track "${track.title}"?`);
    if (!confirmed) {
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.trackService.deleteTrack(track.id).subscribe({
      next: () => {
        this.success.set('Track deleted successfully.');
      },
      error: (err: Error) => {
        this.error.set(err.message);
      },
      complete: () => {
        this.loading.set(false);
        this.loadTracks();
      }
    });
  }
}
