import { AfterViewInit, Component, ElementRef, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AutoCompleteService } from '../../services/autocomplete.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FlightOffer, FlightQuery, UserInfo } from '../../models';
import { FlightService } from '../../services/flight.service';
import { Subscription } from 'rxjs';
import { session } from '../../db/session.repository';
import { UserStore } from '../../store/user.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-flight-search',
  standalone: false,
  templateUrl: './flight-search.component.html',
  styleUrl: './flight-search.component.css'
})
export class FlightSearchComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('dep_airport') depAirportInput!: ElementRef;
  @ViewChild('arr_airport') arrAirportInput!: ElementRef;
  
  private acSvc = inject(AutoCompleteService)
  private fSvc = inject(FlightService)
  private fb = inject(FormBuilder)
  private userStore = inject(UserStore)
  private router = inject(Router)
  
  protected form!: FormGroup
  protected searchSub$!: Subscription
  protected flightsSub$!: Subscription
  protected airportSub$!: Subscription
  protected user!:UserInfo
  protected depAirport?:any
  protected arrAirport?:any
  protected fResults:FlightOffer[] = []

  ngOnInit(): void {
    this.form = this.createForm()
    session.getSession().then(
      u => this.user = u
    )
    this.flightsSub$ = this.userStore.flights$.subscribe(flights => {
      console.log('Current flights:', flights);
    });
  }

  ngAfterViewInit(): void { 
    this.initAutocomplete()
  }

  ngOnDestroy(): void {
    if (this.searchSub$) {
      this.searchSub$.unsubscribe()
    }
    if (this.flightsSub$) {
      this.flightsSub$.unsubscribe()
    }
    if (this.airportSub$) {
      this.airportSub$.unsubscribe()
    }
  }

  chooseFlight(flight:FlightOffer) {
    console.log('Choosing flight:', flight)
    
    // Create a deep copy of the flight to avoid reference issues
    const flightCopy = { ...flight }
    
    this.userStore.saveFlight(flightCopy)
    
    setTimeout(() => {
      this.router.navigate(['/dashboard'])
    }, 200)
  }

  processForm() {
    this.arrAirport = this.form.value.arr_airport
    this.depAirport = this.form.value.dep_airport
    const query:FlightQuery = {
      dep_airport:this.depAirport,
      arr_airport:this.arrAirport,
      dep_date:this.form.value.dep_date,
      arr_date:this.form.value.arr_date,
      trip_type:this.form.value.trip_type,
      passenger:this.form.value.passenger,
      class:this.form.value.class,
      timezone:this.user.timezone,
      currency:this.user.currency
    }

    if (this.searchSub$) {
      this.searchSub$.unsubscribe();
    }

    this.searchSub$ = this.fSvc.searchFlight(query).subscribe(
      (result) => {
        this.fResults = result
        console.info('result: ', this.fResults)
      }
    )
  }

  async initAutocomplete() {
    const inputs = [
      this.depAirportInput.nativeElement,
      this.arrAirportInput.nativeElement
    ]

    await this.acSvc.initializeAutocomplete(inputs)

    this.airportSub$ = this.acSvc.airportSelected.subscribe(result => {
      // Update the appropriate form control based on which input was used
      if (result.inputName === 'dep_airport') {
        this.form.get('dep_airport')?.setValue(result.airport)

      } else if (result.inputName === 'arr_airport') {
        this.form.get('arr_airport')?.setValue(result.airport)
      }
    })
  }
  
  createForm() {
    return this.fb.group({
      dep_airport: this.fb.control<string>('',[Validators.required]),
      arr_airport: this.fb.control<string>('',[Validators.required]),
      dep_date: this.fb.control<string>('',[Validators.required]),
      arr_date: this.fb.control<string>('',[Validators.required]),
      trip_type: this.fb.control<string>('one-way',[Validators.required]),
      passenger: this.fb.control<number>(1,[Validators.required]),
      class: this.fb.control<string>('ECONOMY',[Validators.required])
    })
  }
  testnav() {
    this.router.navigate(['/dashboard'])
  }
}
