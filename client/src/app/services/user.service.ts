import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, firstValueFrom, map, Observable, throwError } from "rxjs";
import { UserInfo } from "../models";

@Injectable()
export class UserService {

    private http = inject(HttpClient)

    createUser(user:UserInfo) {

        return this.http.post('/api/create-user', user)
    }

    validateEmail(email:string):Observable<any> {

        const param = new HttpParams()
            .set("email", email)

        return this.http.get<any>('/api/validate-email', {params: param})
            .pipe(
                catchError(this.handleError)
            )
    }

    handleError(error: HttpErrorResponse) {
        if (error.status === 404 || error.message.includes('email not found'))
            return throwError(() => new Error('email not registered'))
        return throwError(() => new Error('An unexpected error occurred: ' + error.message));
    }
}

