import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, AbstractControl, ValidationErrors, Validators } from '@angular/forms';
import { Country, LoginInfo, UserInfo } from '../../models';
import { UserService } from '../../services/user.service';
import { UserStore } from '../../store/user.store';
import { AutoCompleteService } from '../../services/autocomplete.service';
import { countryDB } from '../../db/country.repository';
import { session } from '../../db/session.repository';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';

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
  private router = inject(Router)
  
  protected signUpForm!:FormGroup
  protected isLogin:boolean = false

  protected filteredLocation: string[] = []
  protected locationMap: Map<string, Country> = new Map()
  protected tz!:string
  protected currency!:string

  ngOnInit(): void {
    this.checkExistingSession()
    this.signUpForm = this.createSignUpForm()
    this.loadCountries()
  }

    async checkExistingSession(): Promise<void> {
      try {
        // Get existing user from session
        const users = await session.session.toArray()
        
        if (users.length > 0) {
          const user: UserInfo = users[0]
    
          if (user) {
            console.log('Active session found, auto-navigating to dashboard')
            
            localStorage.setItem('isAuthenticated', 'true')
            this.userStore.addUser(user)
            
            // Navigate to dashboard
            this.router.navigate(['/dashboard'])
          }
        }
      } catch (error) { console.error('Error checking session:', error) }
    }

  processSignUpForm() {
    const location = this.signUpForm.value.location
    
    //find timezone and currency from indexdb 
    if (this.locationMap.has(location))
    countryDB.countries.toArray()
    .then(allCountries => {
      //find country object with country+city key
      const match = allCountries.find(c => 
        this.formatLocationKey(c) === location
      )
      // console.log('Case-insensitive match:', match)
      const user:LoginInfo = {
        email: this.signUpForm.value.email,
        username: this.signUpForm.value.username,
        password: this.signUpForm.value.password,
        country: match!.country,
        city: match!.city,
        timezone: match!.timezone,
        currency: match!.currency
      }
      console.info('>>>user: ', user)
      return this.userSvc.createUser(user)
    }).then(result => {
      // console.log('User created successfully:', result)
        this.userStore.saveUser(result)
        this.createSignUpForm()
        this.router.navigate(['/dashboard'])
      })
  }

  async loadCountries() {
    // First check if we have countries in DB
    const countries = await firstValueFrom(this.autoSvc.getCountriesFromDB())
    
    if (countries.length === 0) {
      await this.autoSvc.getCountries()
  
      const newCountries = await firstValueFrom(this.autoSvc.getCountriesFromDB())
      
      for (const country of newCountries) {
        this.locationMap.set(this.formatLocationKey(country), country)
      }
      
      console.log('New country sample:', newCountries.length > 0 ? newCountries[0] : 'No data')
      return;
    }
    for (const country of countries) {
      this.locationMap.set(this.formatLocationKey(country), country)
    }
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

  formatLocationKey(country: any): string {
    // If city exists, show "Country, City" format, otherwise just show country
    return country.city ? `${country.country}, ${country.city}` : country.country
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

  isValid() {
    return this.signUpForm.pristine || this.signUpForm.invalid
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value
    const confirmPassword = control.get('confirmPassword')?.value
    
    //console.log('Validator called:', { password, confirmPassword })
    return password === confirmPassword ? null : { passwordMismatch: true }
  }
}
