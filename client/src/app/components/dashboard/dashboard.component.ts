import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { UserStore } from '../../store/user.store';
import { FlightOffer, UserInfo } from '../../models';
import { Subscription } from 'rxjs';
import { flight } from '../../db/flights.repository';
import { session } from '../../db/session.repository';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {
  private userStore = inject(UserStore)
  private router = inject(Router)

  protected user!:UserInfo
  protected flights!:FlightOffer[]
  protected flightsSub$!:Subscription
  
  ngOnInit(): void {
    this.flightsSub$ = this.userStore.flights$.subscribe(flights => {
      this.flights = flights
      console.info('Flights from store:', this.flights)
    })
    flight.getFlights().then(storedFlights => {
      console.info('Flights in storage:', storedFlights)

      if (this.flights.length === 0 && storedFlights.length > 0) {
        storedFlights.forEach(flight => {
          this.userStore.addFlight(flight)
        })
      }
    })
    session.getSession().then(u => {
      this.user = u
      console.info('user: ', this.user)
    })
  }

  ngOnDestroy(): void {
    if (this.flightsSub$)
      this.flightsSub$.unsubscribe()
  }

  checkout() {
    this.router.navigate(['/payment'])
  }

  deleteFlight(flightObj: FlightOffer) {
    console.log('Dashboard - Deleting flight:', JSON.stringify(flightObj, null, 2))
    
    // Create a deep copy to avoid reference issues
    const flightToDelete = { ...flightObj }

    this.userStore.removeFlight(flightToDelete)
  }

  goSearch() {
    this.router.navigate(['/search'])
  }

//confirm then go to payment
}
