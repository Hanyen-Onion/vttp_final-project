import { Component, inject, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from '../../services/user.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserInfo } from '../../models';
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
  
  private authSvc = inject(UserService)
  private fb = inject(FormBuilder)
  private userStore = inject(UserStore)

  protected form!:FormGroup
  protected sub$!:Subscription

  ngOnInit(): void {
    this.form = this.createForm()

    this.initGoogleSignIn()
  }

  processForm(){
    const email = this.form.value.email
    const pw = this.form.value.password

    this.sub$ = this.authSvc.postLogin(email, pw).subscribe({
      next: (data) => {
          const udata = JSON.parse(data)

          const user: UserInfo = {
            email: udata.email || email,
            username: udata.username,
            first_name:udata.first_name,
            last_name:udata.last_name
          }

          this.userStore.createUser(user)
      },
      error: (err) => {
        console.error('err: ', err)
      }
    })

    this.createForm()
  }

  createForm():FormGroup {
    return this.fb.group({
      email: this.fb.control<string>('',[ Validators.required, Validators.email]),
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
