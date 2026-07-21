import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HealthService {
  private readonly apiBaseUrl = 'http://localhost:8080';

  constructor(private readonly http: HttpClient) {}

  checkHealth(): Observable<void> {
    return this.http.get<void>(`${this.apiBaseUrl}/health`);
  }

  checkReady(): Observable<void> {
    return this.http.get<void>(`${this.apiBaseUrl}/ready`);
  }
}
