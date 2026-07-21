import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Scan } from '../../models/scan';

@Injectable({
  providedIn: 'root'
})
export class ScanService {
  private readonly apiBaseUrl = 'http://localhost:8080';

  constructor(private readonly http: HttpClient) {}

  getAllScans(): Observable<Scan[]> {
    return this.http.get<Scan[]>(`${this.apiBaseUrl}/scan`);
  }

  getScanById(id: number): Observable<Scan> {
    return this.http.get<Scan>(`${this.apiBaseUrl}/scan/${id}`);
  }

  createScan(scan: Omit<Scan, 'id' | 'deleted'>): Observable<Scan> {
    return this.http.post<Scan>(`${this.apiBaseUrl}/scan`, scan);
  }

  deleteScan(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/scan/${id}`);
  }

  searchScans(domainName: string, orderBy: string): Observable<Scan[]> {
    return this.http.get<Scan[]>(
      `${this.apiBaseUrl}/scan/search/${encodeURIComponent(domainName)}?orderBy=${encodeURIComponent(orderBy)}`
    );
  }
}
