package com.hpi.tpc.app.security;

import com.hpi.tpc.ui.views.main.LoginView;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.errors.*;
import com.vaadin.flow.server.HandlerHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import java.util.stream.Stream;
import org.springframework.core.annotation.*;
import org.springframework.security.access.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;


/**
 * SecurityUtils takes care of all such static operations that have to do with
 * security and querying rights from different beans of the UI.
 * <p>
 */
@UIScope
@VaadinSessionScope
@Component
public final class SecurityUtils {
    
    private SecurityUtils() {
        // Util methods only
    }

    /**
     * Tests if the request is an internal framework request. The test consists
     * of
     * checking if the request parameter is present and if its value is
     * consistent
     * with any of the request types know.
     *
     * @param request
     *                {@link HttpServletRequest}
     *
     * @return true if is an internal framework request. False otherwise.
     */
    synchronized static boolean isFrameworkInternalRequest(
        HttpServletRequest request) {
        final String parameterValue = request.getParameter(
            ApplicationConstants.REQUEST_TYPE_PARAMETER);
        
        //Use Referer in header to check if it is a service worker initiated request
        String referer = request.getHeader("Referer");
        
        boolean isServiceWorkInitiated = (referer != null&& referer.endsWith("sw.js"));
        
        
        
        return isServiceWorkInitiated
               || parameterValue != null 
                  && Stream.of(RequestType.values())
                      .anyMatch(r ->r.getIdentifier().equals(parameterValue));
    }

    /**
     * Checks if the user is logged in.
     *
     * @return true if the user is logged in. False otherwise.
     */
    public synchronized static boolean isUserLoggedIn() {
        return isUserLoggedIn(SecurityContextHolder.
            getContext().getAuthentication());
    }

    private synchronized static boolean isUserLoggedIn(
        Authentication authentication) {
        return authentication != null &&
               !(authentication instanceof AnonymousAuthenticationToken);
    }

    public synchronized static Integer getUserId() {
        Authentication authentication;
        // CustomUserDetailsService cuds;
        CustomUser customUser;

        authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) return null;
        if (authentication.getPrincipal().toString().equalsIgnoreCase("anonymoususer")){
            //disallow any anonymous users
            return null;
        }
        customUser = (CustomUser) authentication.getPrincipal();

        return customUser.getId();
    }

    /**
     * Gets the user name of the currently signed in user.
     *
     * @return the user name of the current user or <code>null</code> if the
     *         user
     *         has not signed in
     */
    public synchronized static String getUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) context.
                getAuthentication().getPrincipal();
            return userDetails.getUsername();
        }
        // Anonymous or no authentication.
        return null;
    }

    /**
     * Checks if access is granted for the current user for the
     * given secured view, defined by the view class.
     *
     * @param securedClass View class
     *
     * @return true if access is granted, false otherwise.
     */
    public synchronized static boolean isAccessGranted(Class<?> securedClass) {
        final boolean publicView = LoginView.class.equals(securedClass) ||
                                   AccessDeniedView.class.equals(securedClass) ;
                                   //|| CustomRouteNotFoundError.class.equals(securedClass);

        // Always allow access to public views
        if (publicView) {
            return true;
        }

        Authentication userAuthentication = SecurityContextHolder.getContext().
            getAuthentication();

        // All other views require authentication
        if (!isUserLoggedIn(userAuthentication)) {
            return false;
        }

        // Allow if no roles are required.
        Secured secured = AnnotationUtils.findAnnotation(securedClass,
            Secured.class);
        if (secured == null) {
            return true;
        }

        List<String> allowedRoles = Arrays.asList(secured.value());
        return userAuthentication.getAuthorities().stream().map(
            GrantedAuthority::getAuthority)
            .anyMatch(allowedRoles::contains);
    }
}