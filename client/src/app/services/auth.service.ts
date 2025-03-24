import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { LoginInfo } from "../models";

@Injectable()
export class AuthService {

    private http = inject(HttpClient)

    //login auth
    postLogin(user: LoginInfo):Observable<string> {

        const loginInfo = new HttpParams()
            .set('email', user.email)
            .set('password', user.password)

        const headers = new HttpHeaders()
            .set('Content-Type', 'application/x-www-form-urlencoded')
        
        return this.http.post<string>('/api/login', loginInfo.toString(), {headers:headers})
    }

}