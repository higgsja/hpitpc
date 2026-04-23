package com.hpi.tpc.app.security;

import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;

public class SecurityService
{
//    private static final String LOGOUT_SUCCESS_URL = "/";

    public UserDetails getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails;
        }
        // Anonymous or no authentication.
        return null;
    }

//    public void logout() {
//        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
//        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
//        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
//    }
}
