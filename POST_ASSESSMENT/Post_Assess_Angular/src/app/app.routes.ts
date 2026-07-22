import { Routes } from '@angular/router';
import { AddTrackComponent } from './components/add-track/add-track.component';
import { HomeComponent } from './components/home/home.component';
import { SearchTrackComponent } from './components/search-track/search-track.component';
import { TrackDetailsComponent } from './components/track-details/track-details.component';
import { TrackListComponent } from './components/track-list/track-list.component';
import { NotFoundComponent } from './not-found/not-found.component';

export const routes: Routes = [
	{
		path: '',
		component: HomeComponent
	},
	{
		path: 'tracks',
		component: TrackListComponent
	},
	{
		path: 'tracks/add',
		component: AddTrackComponent
	},
	{
		path: 'tracks/search',
		component: SearchTrackComponent
	},
	{
		path: 'tracks/details/:title',
		component: TrackDetailsComponent
	},
	{
		path: '**',
		component: NotFoundComponent
	}
];
