import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { UserInfo } from './models';
import { session } from './db/session.repository';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})

export class AppComponent implements OnInit {
  title = 'client';

  //private httpSvc = inject()
  private activatedRoute = inject(ActivatedRoute)
  private router = inject(Router)

  protected user!:UserInfo

  ngOnInit(): void {
    this.getLoggedInUser
  }

  toDashbaord() {
    this.router.navigate(['/dashboard'])
  }

  async getLoggedInUser() {
    this.user = await session.session.toArray().then(users => users[0]);
    console.info('session user: ', this.user)
  }


}
