import { inject, Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { FlightOffer, UserInfo, UserSlice } from "../models";
import { SessionRepository } from "../db/session.repository";
import { catchError, concatMap, EMPTY, from, map, Observable, of, tap } from "rxjs";
import { FlightRepository } from "../db/flights.repository";

const INIT: UserSlice = {
    user: {
        email: '',
        username: '',
        country: '',
        city: '',
        timezone: '',
        currency: ''
    },
    flights: []
}

@Injectable()
export class UserStore extends ComponentStore<UserSlice> {

    private sessRepo = inject(SessionRepository)
    private fRepo = inject(FlightRepository)

    constructor() {
        super(INIT)
    }

    //after authentication add it in
    readonly addUser = this.updater<UserInfo>(
        (slice:UserSlice, user: UserInfo) => {
            console.info(`save authenticated user ${user.username} to store and db`)
            return {
                user: user,
                flights: [...slice.flights]
            } as UserSlice
        }
    )

    readonly addFlight = this.updater<FlightOffer>(
        (slice:UserSlice, flight:FlightOffer) => {
            return {
                user: slice.user,
                flights: [...slice.flights, flight]
            } as UserSlice
        }
    )

    readonly deleteUser = this.updater<string>(
        () => {
            return INIT
        }
    )

    readonly deleteFlight = this.updater<FlightOffer>(
        (slice:UserSlice, flight:FlightOffer) => ({
            ...slice,
            flights: slice.flights.filter(f => 
                f.depTime !== flight.depTime || 
                f.arrTime !== flight.arrTime || 
                f.carrier !== flight.carrier
            )
        })
    )

    readonly flights$ = this.select(state => state.flights)

    readonly saveFlight = this.effect(
        (newFlight$: Observable<FlightOffer>) => 
            newFlight$.pipe(
                concatMap(f => {
                    const toSave: FlightOffer = {
                        ...f
                    }
                    return from(this.fRepo.saveFlight(f))
                }),
                tap(f => {
                    console.log('Flight saved successfully:', f)
                    this.addFlight(f)}),
                catchError(() => EMPTY) 
            )
    )

    readonly removeFlight = this.effect(
        (flights$: Observable<FlightOffer>) => {
            return flights$.pipe(
                concatMap((f) => {
                    return from(this.fRepo.deleteRepoFlight(f)).pipe(
                        tap(() => console.log('Repository deletion successful')),
                        map(() => f),
                        catchError(error => {
                            console.error('Error removing flight from repository:', error)
                            return EMPTY
                        })
                    )
                }),
                tap(flight => {
                    console.log('Updating store state after successful delete');
                    this.deleteFlight(flight)
                })
            )
        }
    )

    readonly saveUser = this.effect(
        (newUser$: Observable<UserInfo>) => {
            return newUser$.pipe(
                concatMap(user => from(this.sessRepo.saveUserToSession(user))),
                tap(user => this.addUser(user))
            )
        }
    )

    readonly removeUser = this.effect(
        (user$: Observable<UserInfo>) => {
            return user$.pipe(
                concatMap(
                    (user:UserInfo) => from(this.sessRepo.removeUserSession(user))
                ),
                tap(user => this.deleteUser(user.username))
            )
        }
    )



}