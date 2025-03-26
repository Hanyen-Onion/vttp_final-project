import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { FlightOffer } from "../models";

@Injectable()
export class PaymentService {
    private http = inject(HttpClient)

    async createPayment(flights:FlightOffer[]){
        // Create payment intent on the server
        const response = await firstValueFrom(
            this.http.post('/api/payment/create-payment-intent', flights))
    }
}