import { inject, Injectable } from "@angular/core";
import { ComponentStore } from "@ngrx/component-store";
import { FlightOffer, UserInfo, UserSlice } from "../models";
import { SessionRepository } from "../db/session.repository";
import { concatMap, from, Observable, switchMap, tap } from "rxjs";

const INIT: UserSlice = {
    user: {
        email: '',
        username: '',
        location: '',
        timezone: '',
        currency: ''
    },
    flights: []
}

@Injectable()
export class UserStore extends ComponentStore<UserSlice> {

    private sessRepo = inject(SessionRepository)

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

    readonly flights$ = this.select(state => state.flights)

    //this should only save userinfo to db
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