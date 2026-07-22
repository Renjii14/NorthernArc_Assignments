import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';

import { Track } from '../../models/track';
import { TrackService } from '../../services/track.service';
import { EmptyStateComponent } from '../shared/empty-state/empty-state.component';
import { LoadingComponent } from '../shared/loading/loading.component';
import { PageHeaderComponent } from '../shared/page-header/page-header.component';

@Component({
  selector: 'app-track-details',
  standalone: true,
  imports: [DatePipe, RouterLink, LoadingComponent, EmptyStateComponent, PageHeaderComponent],
  templateUrl: './track-details.component.html',
  styleUrl: './track-details.component.css'
})
export class TrackDetailsComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly trackService = inject(TrackService);

  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly tracks = signal<Track[]>([]);
  readonly selectedTrack = signal<Track | null>(null);

  readonly titleParam = signal('');

  ngOnInit(): void {
    const title = this.route.snapshot.paramMap.get('title') ?? '';
    this.titleParam.set(title);

    if (!title) {
      this.error.set('Track title is missing in route.');
      return;
    }

    this.fetchTrackByTitle(title);
  }

  private fetchTrackByTitle(title: string): void {
    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.trackService.searchTrack(title).subscribe({
      next: (track) => {
        this.selectedTrack.set(track);
        this.success.set('Track details loaded successfully.');
      },
      error: (err: Error) => {
        this.selectedTrack.set(null);
        this.error.set(err.message);
      },
      complete: () => {
        this.loading.set(false);
      }
    });
  }
}
