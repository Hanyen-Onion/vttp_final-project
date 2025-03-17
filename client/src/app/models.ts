//user and login
export interface UserInfo {
    email:string 
    username:string
    first_name:string
    last_name:string
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
