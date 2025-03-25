//for both login and signup
export interface LoginInfo {
    email:string 
    username:string
    password:string
    location:string
    timezone:string
    currency:string
}

//session
export interface UserInfo {
    email:string 
    username:string
    location:string
    timezone:string
    currency:string
}

export interface UserSlice {
    user: UserInfo
    flights: FlightOffer[]
}

//flight search
export interface FlightQuery {
    dep_airport:string
    arr_airport:string
    dep_date:string
    arr_date:string
    trip_type:string
    passenger:number
    class:string
    timezone:string
    currency:string
}

export interface FlightOffer {
    duration:string,
    depCode:string,
    depTerminal:string,
    depTime:string, 
    arrCode:string,
    arrTerminal:string,
    arrTime:string,
    price:number,
    currency:string,
    carrier:string,
    date:string
}

export interface AutocompleteResult {
    inputName:string
    airport: string
}

export interface ErrorObject {
    message: string
    status: number
    timestamp: Date
}

export interface Country {
    id?:number
    country: string //singapore,singapore
    timezone: string
    currency: string
}
