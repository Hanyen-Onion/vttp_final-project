import { AfterViewInit, Component, ElementRef, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AutoCompleteService } from '../../services/autocomplete.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FlightQuery } from '../../models';
import { FlightService } from '../../services/flight.service';
import { Subscription } from 'rxjs';

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

  protected form!: FormGroup
  protected sub$!: Subscription
  
  ngOnInit(): void {

    this.form = this.createForm()
  }

  ngAfterViewInit(): void { 
    this.initAutocomplete()
  }

  ngOnDestroy(): void {
    this.sub$.unsubscribe()
  }

  processForm() {
    const query:FlightQuery = this.form.value

    console.log(query)
    this.sub$ = this.fSvc.searchFlight(query).subscribe()
    this.form = this.createForm()
  }

  async initAutocomplete() {
    const inputs = [
      this.depAirportInput.nativeElement,
      this.arrAirportInput.nativeElement
    ]

    await this.acSvc.initializeAutocomplete(inputs)

    this.acSvc.airportSelected.subscribe(result => {
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
      trip_type: this.fb.control<string>('',[Validators.required]),
      passenger: this.fb.control<number>(1,[Validators.required]),
      class: this.fb.control<string>('',[Validators.required])
    })
  }

}
