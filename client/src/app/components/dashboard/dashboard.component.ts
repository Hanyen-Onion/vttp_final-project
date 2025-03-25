import { Component, inject, OnInit } from '@angular/core';
import { UserStore } from '../../store/user.store';
import { FlightOffer, UserInfo } from '../../models';
import { Subscription } from 'rxjs';
import { flight } from '../../db/flights.repository';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{
  private userStore = inject(UserStore)

  protected user!:UserInfo
  protected flights!:FlightOffer[]
  protected sub$!:Subscription
  
  ngOnInit(): void {
    flight.getFlights().then(
      f => this.flights = f
    )
  }
}
