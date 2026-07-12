import { Component } from '@angular/core';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'app-footer',
  standalone: true,
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']})
export class FooterComponent {
  readonly appVersion = 'v2.4.1';
  readonly environmentLabel = environment.production ? 'Production' : 'Non-Production';
  readonly organization = 'Lendora';

  lastSyncTime(): string {
    return new Date().toLocaleString('en-IN');
  }
}

