import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { UserService } from './services/user.service';
import { FlightSearchComponent } from './components/flight-search/flight-search.component';
import { PaymentComponent } from './components/payment/payment.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { providePrimeNG } from 'primeng/config';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import Aura from '@primeng/themes/aura';
import { PrimeModule } from './prime.module';
import { UserStore } from './store/user.store';
import { AutoCompleteService } from './services/autocomplete.service';
import { FlightService } from './services/flight.service';
import { SignupFormComponent } from './components/signup-form/signup-form.component';
import { SessionRepository } from './db/session.repository';
import { CountryRepository } from './db/country.repository';
import { checkIfAuthenticated } from './guard';

const routes: Routes = [
  { path:'', component:LoginFormComponent },
  { path:'login', component:LoginFormComponent },
  { path:'signup', component:SignupFormComponent},
  { path:'dashboard', component:DashboardComponent, canActivate:[checkIfAuthenticated]},
  { path:'search',component:FlightSearchComponent, canActivate:[checkIfAuthenticated]},
  
  { path:'**', redirectTo:'/', pathMatch:'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    DashboardComponent,
    FlightSearchComponent,
    PaymentComponent,
    CalendarComponent,
    SignupFormComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes, {useHash: true}),
    PrimeModule
  ],
  providers: [
    UserStore,
    AutoCompleteService,
    UserService,
    FlightService,
    SessionRepository,
    CountryRepository,
    provideHttpClient(), 
    providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          darkModeSelector: false || 'none'
        }
      }
    }),
    provideAnimationsAsync(),
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
