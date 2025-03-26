import { Component, inject, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Country, LoginInfo, UserInfo } from '../../models';
import { UserStore } from '../../store/user.store';
import { Router } from '@angular/router';
import { session } from '../../db/session.repository';
import { AutoCompleteService } from '../../services/autocomplete.service';
import { countryDB } from '../../db/country.repository';

declare global {
  interface Window {
    google?: any
  }
}

@Component({
  selector: 'app-login-form',
  standalone: false,
  templateUrl: './login-form.component.html',
  styleUrl: './login-form.component.css'
})
export class LoginFormComponent implements OnInit {
  
  private userSvc = inject(UserService)
  private fb = inject(FormBuilder)
  private userStore = inject(UserStore)
  private router = inject(Router)
  private autoSvc = inject(AutoCompleteService)

  protected loginForm!:FormGroup
  protected isLogin:boolean = true
  protected sub$!:Subscription
  protected countries?:Country[]
  

  ngOnInit(): void {
    this.checkExistingSession()
    this.loginForm = this.createLoginForm()
    //this.initGoogleSignIn()
    this.autoSvc.getCountries().then(c => {
      this.countries = c
      console.log('countries: ', this.countries)
    })

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


  processLoginForm(){
    const user: LoginInfo = {
      email:this.loginForm.value.email,
      password: this.loginForm.value.password,
      username: '',
      country: '',
      city: '',
      timezone: '',
      currency: ''
    }
    this.userSvc.postLogin(user).then(
      result => {
        //save to slice
        this.userStore.saveUser(result)
        console.info(result)
        this.router.navigate(['/dashboard'])
    })

    this.createLoginForm()
  }

  isValid() {
    return this.loginForm.pristine || this.loginForm.invalid
  }

  toSignup() {
    this.router.navigate(['/signup'])
  }

  createLoginForm():FormGroup {
    this.isLogin = true
    return this.fb.group({
      email: this.fb.control<string>('',[ Validators.required, Validators.email], [this.userSvc.emailSignedUpValidator(this.isLogin)]),
      password: this.fb.control<string>('', [ Validators.required, 
                                              Validators.minLength(6), 
                                              Validators.maxLength(20)])
    })
  }

  initGoogleSignIn() {
    if (window.google) {
      this.renderSignInButton()
    } else {
      window.addEventListener('load', () => {
        this.renderSignInButton()
      })
    }
  }

  renderSignInButton() {
    const onload = document.getElementById('g_id_onload')
    if (onload) {
      onload.setAttribute('data-client_id',
        '175032234502-fq0i953n2hqqg4k2c83gae6a6ifa1bps.apps.googleusercontent.com')
      onload.setAttribute('data-context','use')
      onload.setAttribute('data-ux_mode','popup')
      onload.setAttribute('data-login_uri','/api/google-login')
      onload.setAttribute('data-auto_select','true')
      onload.setAttribute('data-itp_support','true')
    }

    const signin = document.getElementById('g_id_signin')
    if (signin) {
      signin.setAttribute('data-type', 'standard')
      signin.setAttribute('data-shape', 'pill')
      signin.setAttribute('data-theme', 'outline')
      signin.setAttribute('data-text', 'signin_with')
      signin.setAttribute('data-size', 'large')
      signin.setAttribute('data-logo_alignment','left')
    }
  }
}
