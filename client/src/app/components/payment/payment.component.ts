import { Component, ElementRef, inject, OnInit, ViewChild } from '@angular/core';
import { UserStore } from '../../store/user.store';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FlightOffer, UserInfo } from '../../models';
import { session } from '../../db/session.repository';
import { flight } from '../../db/flights.repository';
import { PaymentService } from '../../services/payment.service';

@Component({
  selector: 'app-payment',
  standalone: false,
  templateUrl: './payment.component.html',
  styleUrl: './payment.component.css'
})
export class PaymentComponent implements OnInit {
  @ViewChild('cardElement') cardElement!: ElementRef;
  protected card: any
  protected cardError?:string
  protected processing = false
  protected completed = false
  protected amount = 1000;
  private userStore = inject(UserStore)
  private fb = inject(FormBuilder)
  private pSvc = inject(PaymentService)
  
  protected form!:FormGroup
  protected user!:UserInfo
  protected flights!:FlightOffer[]

  ngOnInit(): void {
    session.getSession().then( u => {
      this.user = u
      console.info('user: ', this.user)
    })
    flight.getFlights().then( f => {
      this.flights = f
      console.info('flights: ', this.flights)
    })
  
  }

  // async initializeStripe() {
  //   const { stripe, clientSecret } = await this.pSvc.createPayment(this.amount);
  //   const elements = stripe.elements();
    
  //   // Create card element
  //   this.card = elements.create('card');
  //   this.card.mount(this.cardElement.nativeElement);
    
  //   // Add event listener for errors
  //   this.card.on('change', (event) => {
  //     this.cardError = event.error ? event.error.message : ''
  //   })
  // }

  // async processForm() {
  //   this.processing = true;
    
  //   const { stripe, clientSecret } = await this.pSvc.createPayment(this.amount);
    
  //   const result = await stripe.confirmCardPayment(clientSecret, {
  //     payment_method: {
  //       card: this.card
  //     }
  //   });
    
  //   this.processing = false;
    
  //   if (result.error) {
  //     this.cardError = result.error.message;
  //   } else if (result.paymentIntent.status === 'succeeded') {
  //     this.completed = true;
  //     // redirect to confirmation page
  //   }
  // }

  // createForm() {
  //   this.form = this.fb.group({
  //     name: this.fb.control<string>('', [Validators.required]),
  //     email: this.fb.control<string>(this.user.email, [Validators.required]),
  //     cardNumber: this.fb.control<string>('1234 5678 9012 3456',[Validators.required]),
  //     cvv: this.fb.control<string>('CVV',[Validators.required]),
  //     expirationDate: this.fb.control<string>('MM / YY', [Validators.required])
  //   })
  // }
  
}
