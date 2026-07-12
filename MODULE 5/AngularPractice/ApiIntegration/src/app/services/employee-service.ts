import {inject, Injectable, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import EmployeeResponseDTO from '../dto/EmployeeResponseDTO';
import EmployeeRequestDTO from '../dto/EmployeeRequestDTO';


@Injectable({
  providedIn: 'root'
})
export class EmployeeServices {
  private http:HttpClient=inject(HttpClient);
  private employeesChangedSubject = new Subject<void>();

  employeesChanged$ = this.employeesChangedSubject.asObservable();

  notifyEmployeesChanged(): void {
    this.employeesChangedSubject.next();
  }

  getAll():Observable<EmployeeResponseDTO[]>{
    return this.http.get<EmployeeResponseDTO[]>('http://localhost:8080/api/employees');
  }
  getById(id:number):Observable<EmployeeResponseDTO>{
    return this.http.get<EmployeeResponseDTO>(`http://localhost:8080/api/employees/${id}`);
  }
  add(employee: EmployeeRequestDTO): Observable<EmployeeResponseDTO> {
  return this.http.post<EmployeeResponseDTO>(
    'http://localhost:8080/api/employees',
    employee
  );
}
  update(id:number,employee: EmployeeRequestDTO): Observable<EmployeeResponseDTO> {
    return this.http.put<EmployeeResponseDTO>(`http://localhost:8080/api/employees/${id}`, employee);
  }
  delete(id:number): Observable<string> {
    return this.http.delete(`http://localhost:8080/api/employees/${id}`, { responseType: 'text' });
  }

}
 