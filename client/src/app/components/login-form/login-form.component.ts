import { Component, inject, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginInfo } from '../../models';
import { UserStore } from '../../store/user.store';
import { Router } from '@angular/router';

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

  protected loginForm!:FormGroup
  protected isLogin:boolean = true
  protected sub$!:Subscription
  

  ngOnInit(): void {
    this.loginForm = this.createLoginForm()
    //this.initGoogleSignIn()
  }

  processLoginForm(){
    const user: LoginInfo = {
      email:this.loginForm.value.email,
      password: this.loginForm.value.password,
      username: '',
      location: '',
      timezone: '',
      currency: ''
    }
    this.userSvc.postLogin(user).then(
      result => {
        //save to slice
        this.userStore.addUser(result)
        console.info(result)
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
