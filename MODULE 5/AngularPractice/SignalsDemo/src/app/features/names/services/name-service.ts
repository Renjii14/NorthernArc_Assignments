import { Injectable, signal, WritableSignal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NameService {

  private names: WritableSignal<string[]> = signal([
    'Alice',
    'Bob',
    'Charlie',
    'David',
    'Eve'
  ]);

  getNames(): WritableSignal<string[]> {
    return this.names;
  }

  addName(name: string) {
    this.names.set([...this.names(), name]);
  }

  removeName(name: string) {
    this.names.set(this.names().filter(n => n !== name));
  }

  updateName(oldName: string, newName: string) {
    this.names.set(
      this.names().map(name =>
        name === oldName ? newName : name
      )
    );
  }
}