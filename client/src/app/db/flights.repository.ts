import { Injectable } from "@angular/core";
import { Dexie } from "dexie";
import { FlightOffer } from "../models";

@Injectable()
export class FlightRepository extends Dexie {
    flights!: Dexie.Table<FlightOffer, string>

    constructor() {
        super('flightsdb')

        this.version(1).stores({
            flights:'[depTime+arrTime+carrier]'
        })

        this.flights = this.table('flights')
    }

    saveFlight(flight:FlightOffer): Promise<FlightOffer> {
        return this.flights.add(flight).then(() => flight)
    }

    deleteFlight(flight:FlightOffer) {
        this.flights.delete(flight.depTime+flight.arrTime+flight.carrier)
    }

    getFlights(): Promise<FlightOffer[]> {
        return this.flights.toArray()
    }
}
export const flight = new FlightRepository()