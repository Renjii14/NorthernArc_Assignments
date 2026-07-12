import { Component, EventEmitter, Output } from '@angular/core';
import { PersonDTO } from '../../../../types/PersonDTO';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-person',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './add-person.html',
  styleUrl: './add-person.css',
})
export class AddPerson {

  @Output()
  public onAdd = new EventEmitter<PersonDTO>();

  public newid!: number;
  public newname: string = "";
  public newage!: number;
  public newemail: string = "";

  addPerson() {

    if (this.newid <= 0) {
      alert("Id must be greater than 0");
      return;
    }

    if (this.newname.trim() === "") {
      alert("Name is required");
      return;
    }

    if (this.newage <= 0) {
      alert("Age must be greater than 0");
      return;
    }

    if (this.newemail.trim() === "") {
      alert("Email is required");
      return;
    }

    const newPerson: PersonDTO = {
      id: this.newid,
      name: this.newname,
      age: this.newage,
      email: this.newemail
    };

    this.onAdd.emit(newPerson);

    alert("Person added successfully!");

    // Reset the form
    this.newid = 0;
    this.newname = "";
    this.newage = 0;
    this.newemail = "";
  }
}