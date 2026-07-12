import {Component, inject, OnDestroy, OnInit, signal, WritableSignal} from '@angular/core';
import { EmployeeServices } from '../../services/employee-service';
import EmployeeResponseDTO from '../../dto/EmployeeResponseDTO';
import { ShowEmployee } from '../show-employee/show-employee';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-show-employees',
  imports: [ShowEmployee],
  templateUrl: './show-employees.html',
  styleUrls: ['./show-employees.css'],
})
export class ShowEmployees implements OnInit, OnDestroy {

  employeeService:EmployeeServices=inject(EmployeeServices);
  employeesChangedSubscription?: Subscription;

  employees:WritableSignal<EmployeeResponseDTO[]> = signal([]);
  status:WritableSignal<{loading:boolean, error?:string,success?:boolean}> = signal({loading:false, error:'',success:false});
    ngOnInit(): void {
      this.status.set({loading:true});
      setTimeout(() => {
        this.getAllEmp();
    },1000);

      this.employeesChangedSubscription = this.employeeService.employeesChanged$.subscribe(() => {
        this.getAllEmp();
      });
  }
    getAllEmp(){
      this.employeeService.getAll().subscribe(
        {
          next: (data) => {
            this.status.set({loading:false, success:true});
            console.log(data);
            this.employees.set(data);
          },
          error: (err) => {
            console.log(err);
            this.status.set({loading:false, error:err.message});
          },
          complete: () => {
            console.log('GetAll completed');
          }
        }
      );
    }

    onRemoved(id: number): void {
      this.employees.update((current) => current.filter((employee) => employee.id !== id));
    }

    ngOnDestroy(): void {
      this.employeesChangedSubscription?.unsubscribe();
    }

}
 