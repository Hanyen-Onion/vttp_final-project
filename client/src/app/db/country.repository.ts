import { Injectable } from "@angular/core";
import Dexie from "dexie";
import { Country } from "../models";

Injectable()
export class CountryRepository extends Dexie {
    countries!: Dexie.Table<Country, string>

    constructor() {
        super('countrydb');
        this.version(1).stores({
          countries: 'country'
        });
    }
}
export const countryDB = new CountryRepository();