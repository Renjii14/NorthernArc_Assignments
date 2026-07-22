import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { APP_API_BASE_URL } from '../core/api.config';
import { TrackRequest } from '../models/track-request';
import { Track } from '../models/track';

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = inject(APP_API_BASE_URL);

  getAllTracks(): Observable<Track[]> {
    return this.http.get<Track[]>(`${this.baseUrl}/tracks`);
  }

  createTrack(trackRequest: TrackRequest): Observable<Track> {
    return this.http.post<Track>(`${this.baseUrl}/tracks`, trackRequest);
  }

  deleteTrack(trackId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/tracks/${trackId}`);
  }

  searchTrack(title: string): Observable<Track> {
    return this.http.get<Track>(`${this.baseUrl}/tracks/search`, {
      params: { title }
    });
  }
}
