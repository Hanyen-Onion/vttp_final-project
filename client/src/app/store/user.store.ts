import { Injectable } from "@angular/core";
import { ComponentStore, INITIAL_STATE_TOKEN } from "@ngrx/component-store";
import { UserInfo, UserSlice } from "../models";

const INIT: UserSlice = {
    user: {
        email: '',
        username: '',
        first_name: '',
        last_name: ''
    },
    authenticated: false
}

@Injectable()
export class UserStore extends ComponentStore<UserSlice> {

    constructor() {
        super(INIT)
    }

    //after authentication add it in
    readonly createUser = this.updater<UserInfo>(
        (slice:UserSlice, user: UserInfo) => {
            return {
                user: user,
                authenticated: true
            } as UserSlice
        }
    )

}