import { Component, inject, signal, WritableSignal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import EmployeeRequestDTO from '../../dto/EmployeeRequestDTO';
import { EmployeeServices } from '../../services/employee-service';

@Component({
  selector: 'app-update-employee',
  imports: [FormsModule],
  templateUrl: './update-employee.html',
  styleUrl: './update-employee.css',
})
export class UpdateEmployee {
  employeeService: EmployeeServices = inject(EmployeeServices);
  employee: EmployeeRequestDTO = { id: 0, name: '', salary: 0 };

  status: WritableSignal<{ loading: boolean; error?: string; success?: boolean }> = signal({
    loading: false,
    error: '',
    success: false,
  });

  onSubmit(): void {
    if (!this.employee.id || this.employee.id <= 0) {
      this.status.set({ loading: false, error: 'Please enter a valid employee id.', success: false });
      return;
    }

    this.status.set({ loading: true });

    this.employeeService.update(this.employee.id, this.employee).subscribe({
      next: () => {
        this.status.set({ loading: false, success: true, error: '' });
        this.employeeService.notifyEmployeesChanged();
      },
      error: (err: unknown) => {
        console.error('Error updating employee:', err);
        this.status.set({ loading: false, error: 'Failed to update employee', success: false });
      },
    });
  }

  onReset(): void {
    this.employee = { id: 0, name: '', salary: 0 };
    this.status.set({ loading: false, error: '', success: false });
  }
}
