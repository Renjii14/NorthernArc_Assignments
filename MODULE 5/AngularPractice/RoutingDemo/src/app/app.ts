import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet,RouterLink,RouterLinkActive,FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
router=inject(Router);
searchTerm=signal('');
showPerson(){
  if(this.searchTerm()=='Renjitha'){
    this.router.navigate(['/person', 1]);
  }
}
}

//how to read path variable