import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home';
import { ScanListComponent } from './components/scan/scan-list/scan-list';
import { AddScanComponent } from './components/scan/add-scan/add-scan';
import { SearchScanComponent } from './components/scan/search-scan/search-scan';
import { ScanDetailsComponent } from './components/scan/scan-details/scan-details';
import { SystemHealthComponent } from './components/system-health/system-health';
import { NotFoundComponent } from './components/not-found/not-found';

export const routes: Routes = [
	{ path: '', component: HomeComponent },
	{ path: 'scans', component: ScanListComponent },
	{ path: 'scans/add', component: AddScanComponent },
	{ path: 'scans/search', component: SearchScanComponent },
	{ path: 'scans/:id', component: ScanDetailsComponent },
	{ path: 'system-health', component: SystemHealthComponent },
	{ path: '**', component: NotFoundComponent }
];
