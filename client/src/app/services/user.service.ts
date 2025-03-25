import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, debounceTime, first, firstValueFrom, map, Observable, of, switchMap, tap, throwError } from "rxjs";
import { LoginInfo, UserInfo } from "../models";
import { AsyncValidatorFn, AbstractControl, ValidationErrors } from "@angular/forms";

@Injectable()
export class UserService {

  private http = inject(HttpClient)

  createUser(user:LoginInfo): Promise<UserInfo> {
    return firstValueFrom(this.http.post<UserInfo>('/api/create-user', user))
  }

  //login auth
  postLogin(user: LoginInfo):Promise<UserInfo> {
    const loginInfo = new HttpParams()
        .set('email', user.email)
        .set('password', user.password)

    const headers = new HttpHeaders()
        .set('Content-Type', 'application/x-www-form-urlencoded')
    
    return firstValueFrom(this.http.post<UserInfo>('/api/login', loginInfo.toString(), {headers:headers}))
  }
  
  emailSignedUpValidator(isLogin: boolean): AsyncValidatorFn {
    return (control: AbstractControl): Observable<ValidationErrors | null> => {
      if (!control.value) {
        return of(null)
      }
      return this.validateEmail(control.value).pipe(
        debounceTime(300),
        map(response => {
          // Email exists (API call successful)
          if (isLogin) {
            return null
          } else {
            return { emailExists: true }
          }
        }),
        catchError(error => {
          if (error.error && error.error.message && 
            error.error.message.includes('is not registered')) {
            if (isLogin) {
              return of({ emailNotFound: true })
            } else {
              return of(null)
            }
          } else if (error.status === 500) {
            console.error('Server error during email validation:', error)
            if (!isLogin && error.error && error.error.message && 
              error.error.message.includes('not registered')) 
              return of(null)
            return of(null)
          }
          return of(null)
        }),
        first()
      )
    }
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
    console.log('Raw error from API:', error);
    
    // Check both error.message and error.error.message (where the actual server message is)
    const errorMessage = error.error?.message || error.message || '';
    
    if (error.status === 404 || 
        errorMessage.includes('not found') ||
        errorMessage.includes('not registered')) {
        return throwError(() => new Error('email not registered'))
    } else if (error.status === 500) {
        // Check if this 500 error is actually about email not being registered
        if (errorMessage.includes('not found') || 
            errorMessage.includes('not registered')) {
            return throwError(() => new Error('email not registered'))
        }
        return throwError(() => new Error('server error'))
    }
    return throwError(() => new Error('An unexpected error occurred: ' + errorMessage))
  }

}

