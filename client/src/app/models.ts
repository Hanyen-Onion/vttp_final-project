//user and login
export interface UserInfo {
    email:string 
    username:string
}

export interface LoginInfo {
    email:string 
    username:string
    password:string
}

export interface UserSlice {
    user: UserInfo
    authenticated:boolean
}

//flight search and result
export interface FlightQuery {
    dep_airport:string
    arr_airport:string
    dep_date:string
    arr_date:string
    trip_type:string
    passenger:number
    class:string
}

export interface AutocompleteResult {
    inputName:string
    airport: string
}

export interface FlightDetail {

}
// departure_airport_code:string
// arrival_airport_code:string
// departure_date:string
// number_of_adults:number
// cabin_class:string
// currency:string
// region:string

export interface ErrorObject {
    message: string;
    status: number;
    timestamp: Date;
}
