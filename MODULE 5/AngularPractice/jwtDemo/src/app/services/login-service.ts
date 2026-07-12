import { inject, Injectable, signal, WritableSignal } from '@angular/core';
import { Observable } from 'rxjs';
import JwtResponseDTO from '../dto/JwtResponseDTO';
import { HttpClient } from '@angular/common/http';
import JwtRequestDTO from '../dto/JwtRequestDTO';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  http:HttpClient = inject(HttpClient);
  private apiUrl:string ="http://localhost:8080";
  login(credentials:JwtRequestDTO):Observable<JwtResponseDTO>{
    return this.http.post<JwtResponseDTO>(`${this.apiUrl}/auth/login`,credentials);
  }
}
