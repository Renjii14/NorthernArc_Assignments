import { Component, inject } from '@angular/core';
import { NameService } from '../../services/name-service';

@Component({
  selector: 'app-show-name',
  imports: [],
  templateUrl: './show-name.html',
  styleUrl: './show-name.css',
})
export class ShowName {
  nameService:NameService=inject(NameService);
}
