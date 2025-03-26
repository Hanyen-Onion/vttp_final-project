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

    generateKey(flight: FlightOffer): string {
        return `${flight.depTime},${flight.arrTime},${flight.carrier}`
    }

    deleteRepoFlight(flight:FlightOffer): Promise<void> {
        // Get all flights to find the exact one to delete
        return this.flights.where('[depTime+arrTime+carrier]')
            .equals([flight.depTime, flight.arrTime, flight.carrier])
            .delete()
            .then((deleteCount) => {
                console.log(`DB Delete: Success - deleted ${deleteCount} flight(s)`)
                if (deleteCount === 0) {
                    console.warn('No flights were deleted - key may not exist')
                }
            })
            .catch(error => {
                console.error('DB Delete: Error deleting flight:', error)
                console.error('Flight that failed to delete:', JSON.stringify(flight, null, 2))
                throw error
            })
    }

    getFlights(): Promise<FlightOffer[]> {
        return this.flights.toArray()
    }
}
export const flight = new FlightRepository()