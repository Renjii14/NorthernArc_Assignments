import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PersonDTO } from '../../../../types/PersonDTO';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-update-person',
  imports: [FormsModule],
  templateUrl: './update-person.html',
  styleUrl: './update-person.css',
})
export class UpdatePerson {
  @Input()
  people: PersonDTO[] = [];

  @Output()
  onUpdate = new EventEmitter<PersonDTO>();

  public updateid!: number;
  public updatename = "";
  public updateage!: number;
  public updateemail = "";

  searchPerson() {

    const person = this.people.find(p => p.id === this.updateid);

    if (person) {
      this.updatename = person.name;
      this.updateage = person.age;
      this.updateemail = person.email;
    } else {
      alert("Person not found");
      this.updatename = "";
      this.updateage = 0;
      this.updateemail = "";
    }
  }

  updatePerson() {

    const updatedPerson: PersonDTO = {
      id: this.updateid,
      name: this.updatename,
      age: this.updateage,
      email: this.updateemail
    };

    this.onUpdate.emit(updatedPerson);

    alert("Updated Successfully");

    this.updateid = 0;
    this.updatename = "";
    this.updateage = 0;
    this.updateemail = "";
  }
}
