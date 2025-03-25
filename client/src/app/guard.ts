import { inject } from "@angular/core"
import { ActivatedRouteSnapshot, Router, RouterStateSnapshot } from "@angular/router"
import { session } from "./db/session.repository"
import { UserInfo } from "./models"

export const checkIfAuthenticated = async (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
    const router = inject(Router)

    const users = await session.session.toArray()
    const user: UserInfo = users[0]
    
    if (user) {
        return true;
    }
    
    return router.navigate([''])
    
}