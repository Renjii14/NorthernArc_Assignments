import { Component, inject, OnInit, signal, WritableSignal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import EmployeeDTO from './dto/EmployeeDTO';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './app.html',
})
export class App implements OnInit{
  
  ngOnInit(): void {
    this.getAll();
  }

  protected names:WritableSignal<string[]> = signal([]);
  httpClient: HttpClient=inject(HttpClient);
  protected title = 'FirstDemo';
  protected person:{fname:string,lname:string}={fname:"Lavanya",lname:"Elavarasan"};
  protected newName:string='';
  protected employees: { id: number; fname: string; lname: string; age: number }[] = [
  {
    id: 1,
    fname: "Lavanya",
    lname: "Elavarasan",
    age: 22
  },
  {
    id: 2,
    fname: "Yuva",
    lname: "Priya",
    age: 23
  },
  {
    id: 3,
    fname: "Vishnu",
    lname: "Balaji",
    age: 24
  },
  {
    id: 4,
    fname: "Renjitha",
    lname: "K",
    age: 22
  }
];
 

  changeTitle() {
    this.title = 'Hello, FirstDemo';
}

  removeName(name: string) {
    this.names.set(this.names().filter(n => n !== name));
  }

  removeEmployee(id: number) {
    this.employees = this.employees.filter(emp => emp.id !== id);
  
}

addName() {
  if(this.newName.trim()!=''){
    this.names.set([...this.names(), this.newName]);
  }
this.newName='';
}

updateName(index: number) {
  if (this.newName.trim() != '') {
    const updatedNames = [...this.names()];
    updatedNames[index] = this.newName;
    this.names.set(updatedNames);
    this.newName = '';
  }
}

updateEmployee(id: number) {
  const emp = this.employees.find(e => e.id === id);

  if (emp) {
    emp.fname = prompt("Enter First Name", emp.fname) || emp.fname;
    emp.lname = prompt("Enter Last Name", emp.lname) || emp.lname;
    emp.age = Number(prompt("Enter Age", emp.age.toString())) || emp.age;
  }
}


addEmployee() {

  const fname = prompt("Enter First Name");
  const lname = prompt("Enter Last Name");
  const age = Number(prompt("Enter Age"));

  if (fname && lname && age) {

    const newId = this.employees.length + 1;

    this.employees.push({
      id: newId,
      fname: fname,
      lname: lname,
      age: age
    });

  }
}


getAll(){
  this.httpClient.get<EmployeeDTO[]>('http://localhost:8080/api/employees')
  .subscribe({
    next: (data) => {
      console.log('Employees fetched successfully', data);
      this.names.set(data.map((item:EmployeeDTO)=>item.name));
    },
    error: (err) => {
      console.error('Error fetching employees', err);
    }
  });
}




}