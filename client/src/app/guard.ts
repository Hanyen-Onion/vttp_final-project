import { inject } from "@angular/core"
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot, UrlTree } from "@angular/router"
import { session } from "./db/session.repository"
import { UserInfo } from "./models"
import { state } from "@angular/animations"

export const checkIfAuthenticated = 
    async (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean | UrlTree> => {
    const router = inject(Router)

    if (localStorage.getItem('isAuthenticated') === 'true') {
        return true
    }

    try {
        const users = await session.session.toArray();
        const user: UserInfo = users[0];
        
        if (user) {
          // Make sure localStorage is in sync with session
          localStorage.setItem('isAuthenticated', 'true');
          return true
        }
        return router.parseUrl('')
    } catch (error) {
        console.error('Error checking authentication:', error);
        return router.parseUrl('');
    }
    
}