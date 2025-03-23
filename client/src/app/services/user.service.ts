import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, firstValueFrom, map, Observable, throwError } from "rxjs";

@Injectable()
export class UserService {

    private http = inject(HttpClient)

    validateEmail(email:string):Observable<any> {

        const param = new HttpParams()
            .set("email", email)

        return this.http.get<any>('/api/validate-email', {params: param})
            .pipe(
                catchError(this.handleError)
            )
    }

    //for user that did not sign in with google
    postLogin(email:string, password:string):Observable<string> {

        const loginInfo = new HttpParams()
            .set('email', email)
            .set('password', password)

        const headers = new HttpHeaders()
            .set('Content-Type', 'application/x-www-form-urlencoded')
        
        return this.http.post<string>('/api/login', loginInfo.toString(), {headers:headers})
    }

    handleError(error: HttpErrorResponse) {
        if (error.status === 404 || error.message.includes('email not found'))
            return throwError(() => new Error('email not registered'))
        return throwError(() => new Error('An unexpected error occurred: ' + error.message));
    }
}

