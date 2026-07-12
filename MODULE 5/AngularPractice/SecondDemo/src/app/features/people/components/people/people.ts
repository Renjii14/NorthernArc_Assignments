import { Component } from '@angular/core';
import { Person } from '../person/person';
import { AddPerson } from '../add-person/add-person';
import { PersonDTO } from '../../../../types/PersonDTO';
import { UpdatePerson } from '../update-person/update-person';

@Component({
  selector: 'app-people',
  imports: [Person, AddPerson,UpdatePerson],
  templateUrl: './people.html',
  styleUrl: './people.css',
})
export class People {
   protected people = [
    {
      id: 1,
      name: 'John',
      age: 25,
      email: 'john@gmail.com'
    },
    {
      id: 2,
      name: 'Alice',
      age: 30,
      email: 'alice@gmail.com'
    }
  ];

  protected removePerson(pid: number): void {
    console.log(`Removing person with id: ${pid}`);
    this.people = this.people.filter(person => person.id !== pid);
}

addPerson(person: PersonDTO) {

    const exists = this.people.some(p => p.id === person.id);

    if (exists) {
      alert("Person with this Id already exists.");
      return;
    }

    this.people.push(person);
  }

  updatePerson(updatedPerson: PersonDTO) {

    const index = this.people.findIndex(p => p.id === updatedPerson.id);

    if (index === -1) {
      alert("Person not found.");
      return;
    }

    this.people[index] = updatedPerson;

    alert("Person updated successfully.");
  }

}

