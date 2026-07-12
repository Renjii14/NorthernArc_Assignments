import { Component, inject } from '@angular/core';
import { NameService } from '../../services/name-service';

@Component({
  selector: 'app-add-name',
  imports: [],
  templateUrl: './add-name.html',
  styleUrl: './add-name.css',
})
export class AddName {
  nameService:NameService=inject(NameService);
  
  
}
