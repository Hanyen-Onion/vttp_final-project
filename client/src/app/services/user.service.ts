import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, map, Observable } from "rxjs";

@Injectable()
export class UserService {

    private http = inject(HttpClient)

    //for user that did not sign in with google
    postLogin(email:string, password:string):Observable<string> {

        const loginInfo = new HttpParams()
            .set('email', email)
            .set('password', password)

        const headers = new HttpHeaders()
            .set('Content-Type', 'application/x-www-form-urlencoded')
        
        return this.http.post<string>('/api/login', loginInfo.toString(), {headers:headers})
    }
}