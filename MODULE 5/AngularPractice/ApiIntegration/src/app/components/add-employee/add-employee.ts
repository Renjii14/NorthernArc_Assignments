import { Component, inject, signal, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import EmployeeRequestDTO from '../../dto/EmployeeRequestDTO';
import { EmployeeServices } from '../../services/employee-service';

@Component({
  selector: 'app-add-employee',
  imports: [FormsModule],
  templateUrl: './add-employee.html',
  styleUrl: './add-employee.css',
})
export class AddEmployee {
  employeeService: EmployeeServices = inject(EmployeeServices);
  employee: EmployeeRequestDTO = { id: 0, name: '', salary: 0 };
  status: WritableSignal<{ loading: boolean; error?: string; success?: boolean }> = signal({
    loading: false,
    error: '',
    success: false,
  });

  onSubmit(): void {
    this.status.set({ loading: true });
    console.log('Employee added successfully');

    this.employeeService.add(this.employee).subscribe({
      next: (data) => {
        this.status.set({ loading: false, success: true });
        console.log('Employee added successfully', data);
        this.employeeService.notifyEmployeesChanged();
        this.employee = { id: 0, name: '', salary: 0 };
      },
      error: (err: unknown) => {
        console.error('Error adding employee:', err);
        this.status.set({
          loading: false,
          error: 'Failed to add employee',
        });
      },
    });
  }

  onReset(): void {
    this.employee = { id: 0, name: '', salary: 0 };
    this.status.set({ loading: false, error: '', success: false });
  }
}
