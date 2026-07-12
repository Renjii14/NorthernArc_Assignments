import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PersonDTO } from '../../../../types/PersonDTO';

@Component({
  selector: 'app-person',
  imports: [],
  templateUrl: './person.html',
  styleUrl: './person.css',
})
export class Person {
  @Input()
  public p!: PersonDTO;

  @Output()
  public onRemove = new EventEmitter<number>();
  public removePerson(pid: number): void {
    console.log(`Removing person with id: ${pid}`);
    this.onRemove.emit(pid);
  }

}
