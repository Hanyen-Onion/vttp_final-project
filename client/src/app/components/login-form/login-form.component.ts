import { Component, inject, OnInit } from '@angular/core';
import { catchError, debounce, debounceTime, first, Observable, of, Subscription, switchMap } from 'rxjs';
import { UserService } from '../../services/user.service';
import { AbstractControl, AsyncValidatorFn, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { LoginInfo, UserInfo } from '../../models';
import { UserStore } from '../../store/user.store';

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

  protected loginForm!:FormGroup
  protected signUpForm!:FormGroup
  protected sub$!:Subscription
  protected isLogin:boolean = false

  ngOnInit(): void {
    this.loginForm = this.createLoginForm()
    this.signUpForm = this.createSignUpForm()
    //this.initGoogleSignIn()
  }

  processLoginForm(){
    const email = this.loginForm.value.email
    const pw = this.loginForm.value.password

    this.createLoginForm()
  }

  processSignUpForm() {
    const user:LoginInfo = {
      email: this.signUpForm.value.email,
      username: this.signUpForm.value.username,
      password: this.signUpForm.value.password
    }


    this.createSignUpForm()
  }

  createLoginForm():FormGroup {
    this.isLogin = true
    return this.fb.group({
      email: this.fb.control<string>('',[ Validators.required, Validators.email], [this.emailSignedUpValidator()] ),
      password: this.fb.control<string>('', [ Validators.required, 
                                              Validators.minLength(6), 
                                              Validators.maxLength(20)])
    })
  }

  createSignUpForm() :FormGroup {
    return this.fb.group({
      username: this.fb.control<string>('', [Validators.required, Validators.minLength(4)]),
      email: this.fb.control<string>('',[ Validators.required, Validators.email], [this.emailSignedUpValidator()]),
      password: this.fb.control<string>('', [ Validators.required, 
                                              Validators.minLength(8), 
                                              Validators.maxLength(20)]),
      confirmPassword: this.fb.control<string>('', [ Validators.required, 
                                                Validators.minLength(8), 
                                                Validators.maxLength(20)])
    }, 
    { validators:this.passwordMatchValidator})
  }

  emailSignedUpValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if (!control.value) {
        return of(null);
      }
      return this.userSvc.validateEmail(control.value).pipe(
        debounceTime(300),
        switchMap(() => {
          //for login
          if (this.isLogin)
            return of(null)
          else
            //for signup
            return of({ emailExists: true})
        }),
        catchError(error => {
          if (error.message === 'email not registered') {
            if (this.isLogin)
              return of({ emailNotFound: true })
            else
            return of(null)
          }
          console.error('Email validation error:', error)
          return of({ serverError: true })
        }),
        first() 
      )
    }
  }

  passwordMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmPassword = control.get('confirmPassword')?.value;
    
    return password === confirmPassword ? null : { passwordMismatch: true };
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
