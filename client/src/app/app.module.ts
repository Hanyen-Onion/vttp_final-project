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
import { FlightListComponent } from './components/flight-list/flight-list.component';
import { FlightDetailsComponent } from './components/flight-details/flight-details.component';
import { PaymentComponent } from './components/payment/payment.component';
import { CalendarComponent } from './components/calendar/calendar.component';
import { providePrimeNG } from 'primeng/config';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import Aura from '@primeng/themes/aura';
import { PrimeModule } from './prime.module';
import { UserStore } from './store/user.store';
import { AutoCompleteService } from './services/autocomplete.service';
import { FlightService } from './services/flight.service';


const routes: Routes = [
  { path:'', component:LoginFormComponent },
  { path:'login', component:LoginFormComponent },
  {path:'dashboard', component:DashboardComponent},
  {path:'search',component:FlightSearchComponent},
  
  { path:'**', redirectTo:'/', pathMatch:'full' }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginFormComponent,
    DashboardComponent,
    FlightSearchComponent,
    FlightListComponent,
    FlightDetailsComponent,
    PaymentComponent,
    CalendarComponent,
  ],
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes),
    PrimeModule
  ],
  providers: [
    UserStore,
    AutoCompleteService,
    UserService,
    FlightService,
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
    UserService
    
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
