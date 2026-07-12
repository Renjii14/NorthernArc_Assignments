import { Routes } from '@angular/router';


import {Home} from './components/home/home';

import {About} from './components/about/about';

import {Services} from './components/services/services';

import {Contact} from './components/contact/contact';
 
export const routes: Routes = [

  {path: '', component: Home},//this is eager loading

  {path: 'about', component: About},

  {path: 'services', component: Services},

  {path:'contact',loadComponent: () => import('./components/contact/contact').then(m => m.Contact)},
 
//   this is lazy loading

//   {path:'contact',loadComponent: () => import('./components/contact/contact').then(m => m.Contact)}

//redirect always give atlast

{path:'person/:id', loadComponent: () => import('./components/person/person').then(m => m.Person)},//dynamic route

  {path: '**', redirectTo: ''}

];

 