import { Routes } from '@angular/router';
import { LoginComponent } from './components/login-component/login-component';
import { WelcomeComponent } from './components/welcome-component/welcome-component';
import { LogoutComponent } from './components/logout-component/logout-component';

export const routes: Routes = [
   { path:"login" , component:LoginComponent},
   { path:"welcome" , component:WelcomeComponent},
   {path:'logout',component:LogoutComponent}
];
