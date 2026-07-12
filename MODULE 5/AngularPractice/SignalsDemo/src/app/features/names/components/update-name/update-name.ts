import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NameService } from '../../services/name-service';


@Component({
  selector: 'app-update-name',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-name.html',
  styleUrl: './update-name.css'
})
export class UpdateName {

  nameService = inject(NameService);

  names = this.nameService.getNames();

  oldName = '';
  newName = '';

  update() {
    if (this.oldName.trim() && this.newName.trim()) {
      this.nameService.updateName(this.oldName, this.newName);

      this.oldName = '';
      this.newName = '';
    }
  }
}