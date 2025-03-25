import { Injectable } from "@angular/core";
import Dexie, { Table } from "dexie";
import { FlightOffer, UserInfo, UserSlice } from "../models";

@Injectable()
export class SessionRepository extends Dexie {

    session!: Dexie.Table<UserInfo, string>

    constructor() {
        super('sessiondb')

        this.version(1).stores({
            session:'username'
        })

        this.session = this.table('session')
    }

    getSession() {
        return this.session.toArray().then(user => user[0])
    }

    saveUserToSession(user:UserInfo): Promise<UserInfo> {
        return this.session.add(user).then(() => user)
    }

    removeUserSession(user: UserInfo) {
        return this.session.delete(user.username).then(() => user)
    }
}
export const session = new SessionRepository()