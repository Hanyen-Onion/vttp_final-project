import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { FlightDetail, FlightQuery } from "../models";

Injectable()
export class FlightService {

    private http = inject(HttpClient)

    searchFlight(query:FlightQuery): Observable<FlightDetail> {

        const param = new HttpParams()
            .set('dep_airport', query.dep_airport)
            .set('arr_airport', query.arr_airport)
            .set('dep_date', query.dep_date)
            .set('arr_date', query.arr_date)
            .set('trip_type', query.trip_type)
            .set('passenger', query.passenger)
            .set('class', query.class)

        console.info(param)
        
        const header = new HttpHeaders()
            .set('Content-Type', 'application/json')

        return this.http.get<FlightDetail>('/api/search', { headers:header ,params: param })
    }
}

