import { DatePipe } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterLink } from '@angular/router';

import { Track } from '../../models/track';
import { TrackService } from '../../services/track.service';
import { LoadingComponent } from '../shared/loading/loading.component';
import { PageHeaderComponent } from '../shared/page-header/page-header.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, DatePipe, LoadingComponent, PageHeaderComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  private readonly trackService = inject(TrackService);

  readonly loading = signal(false);
  readonly error = signal('');
  readonly success = signal('');
  readonly tracks = signal<Track[]>([]);
  readonly selectedTrack = signal<Track | null>(null);

  readonly totalTracks = signal(0);
  readonly recentTracks = signal<Track[]>([]);

  ngOnInit(): void {
    this.fetchTracks();
  }

  private fetchTracks(): void {
    this.loading.set(true);
    this.error.set('');
    this.success.set('');

    this.trackService.getAllTracks().subscribe({
      next: (tracks) => {
        this.tracks.set(tracks);
        this.totalTracks.set(tracks.length);
        this.recentTracks.set(tracks.slice(0, 5));
        this.success.set('Dashboard loaded successfully.');
      },
      error: (err: Error) => {
        this.error.set(err.message);
      },
      complete: () => {
        this.loading.set(false);
      }
    });
  }
}
