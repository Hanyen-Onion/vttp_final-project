import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

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

  protected sub$!:Subscription

  ngOnInit(): void {
    
  }


}
