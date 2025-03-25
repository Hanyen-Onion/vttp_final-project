import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, ValidationErrors, Validators } from '@angular/forms';
import { Country, LoginInfo } from '../../models';
import { UserService } from '../../services/user.service';
import { UserStore } from '../../store/user.store';
import { AutoCompleteService } from '../../services/autocomplete.service';
import { countryDB } from '../../db/country.repository';

@Component({
  selector: 'app-signup-form',
  standalone: false,
  templateUrl: './signup-form.component.html',
  styleUrl: './signup-form.component.css'
})
export class SignupFormComponent implements OnInit{

  private userSvc = inject(UserService)
  private autoSvc = inject(AutoCompleteService)
  private fb = inject(FormBuilder)
  private userStore = inject(UserStore)
  
  protected signUpForm!:FormGroup
  protected isLogin:boolean = false

  protected filteredLocation: string[] = []
  protected locationMap: Map<string, Country> = new Map()
  protected tz!:string
  protected currency!:string

  ngOnInit(): void {
    this.signUpForm = this.createSignUpForm()
    this.loadCountries()
  }

  processSignUpForm() {
    const location = this.signUpForm.value.location
    
    //find timezone and currency from indexdb 
    countryDB.countries.toArray()
    .then(allCountries => {
      
      const match = allCountries.find(c => 
        c.country.toLowerCase() === location.toLowerCase()
      )
      //console.log('Case-insensitive match:', match)
      const user:LoginInfo = {
        email: this.signUpForm.value.email,
        username: this.signUpForm.value.username,
        password: this.signUpForm.value.password,
        location: this.signUpForm.value.location,
        timezone: match!.timezone,
        currency: match!.currency
      }
      //console.info('>>>user: ', user)
      return this.userSvc.createUser(user)
    }).then(result => {
      console.log('User created successfully:', result)
        this.userStore.saveUser(result)
        this.createSignUpForm()
      })
  }

  async loadCountries() {
    //from db
    const countries = this.autoSvc.getCountriesFromDB().subscribe(
      (countries) => {
        //console.log('Initial countries from DB:', countries)
        if (countries.length === 0) {
          this.autoSvc.getCountries()
          const newCountries = this.autoSvc.getCountriesFromDB().subscribe(
            newC => {
              for (const country of newC) 
                this.locationMap.set(country.country, country)
              //console.log('New country sample:', newC.length > 0 ? newC[0] : 'No data')
          })
        }
        for (const country of countries) 
          this.locationMap.set(country.country, country)
    })
  }

  filterLocation(event: any): void {
    const query = event.query.toLowerCase()
    
    if (!query) {
      this.filteredLocation = Array.from(this.locationMap.keys()).slice(0, 10)
      return
    }
    
    this.filteredLocation = Array.from(this.locationMap.keys())
      .filter(locat => locat.toLowerCase().startsWith(query))
      .slice(0, 10)
  }

  isValid() {
    return this.signUpForm.pristine || this.signUpForm.invalid
  }

  createSignUpForm() :FormGroup {
    return this.fb.group({
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(4)]),
      email: this.fb.control<string>('',[ Validators.required, Validators.email], [this.userSvc.emailSignedUpValidator(false)]),
      password: this.fb.control<string>('', [ Validators.required, 
                                              Validators.minLength(8), 
                                              Validators.maxLength(20)]),
      confirmPassword: this.fb.control<string>('', [ Validators.required, 
                                                Validators.minLength(8), 
                                                Validators.maxLength(20)]),
      location: this.fb.control<string>('', [Validators.required])
    }, 
    { validators:this.passwordMatchValidator})
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value
    const confirmPassword = control.get('confirmPassword')?.value
    
    //console.log('Validator called:', { password, confirmPassword })
    return password === confirmPassword ? null : { passwordMismatch: true }
  }
}
