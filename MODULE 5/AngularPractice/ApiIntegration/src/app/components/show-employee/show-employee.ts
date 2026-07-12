import { Component, EventEmitter, inject, Input, Output, signal, WritableSignal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { EmployeeServices } from '../../services/employee-service';
import EmployeeResponseDTO from '../../dto/EmployeeResponseDTO';


@Component({
  selector: 'app-show-employee',
  imports: [],
  templateUrl: './show-employee.html',
  styleUrl: './show-employee.css',
})
export class ShowEmployee {
 employeeService:EmployeeServices=inject(EmployeeServices);
 @Input() employee!:EmployeeResponseDTO;
 @Output() removed = new EventEmitter<number>();
 
  status:WritableSignal<{loading:boolean, error?:string,success?:boolean}>=signal({loading:false, error:'',success:false});

  remove(){
    this.status.set({loading:true});
    if(this.employee.id!==undefined && this.employee.id!==null){
      this.employeeService.delete(this.employee.id).subscribe(
        {
          next: () => {
            this.status.set({loading:false, success:true});
            console.log('Employee deleted successfully');
            this.removed.emit(this.employee.id);
            this.employeeService.notifyEmployeesChanged();
          },
          error: (err: HttpErrorResponse) => {
  console.error('Error deleting employee:', err);

  const backendMessage = typeof err.error === 'string' ? err.error : err.message;

  this.status.set({
    loading: false,
    error: backendMessage || 'Failed to delete employee'
  });
},
          complete: () => {
            console.log('Delete completed');
          }
        }
      );
    } else {
      this.status.set({
        loading: false,
        error: 'Invalid employee id for delete',
      });
    }
  }
  
}
