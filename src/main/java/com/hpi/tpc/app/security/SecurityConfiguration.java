package com.hpi.tpc.app.security;

import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.spring.security.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form</li>
 * <p>
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity
{

    /**
     * Require login to access internal pages and configure login form.
     *
     * @param http
     *
     * @throws java.lang.Exception
     */
    @Override
    protected void configure(HttpSecurity http)
        throws Exception
    {
        // Delegating the responsibility of general configurations
        // of http security to the super class. It's configuring
        // the followings: Vaadin's CSRF protection by ignoring
        // framework's internal requests, default request cache,
        // ignoring public views annotated with @AnonymousAllowed,
        // restricting access to other views/endpoints, and enabling
        // ViewAccessChecker authorization.
        // You can add any possible extra configurations of your own
        // here (the following is just an example):

//        http.rememberMe().alwaysRemember(false);
        // Configure your static resources with public access before calling
        // super.configure(HttpSecurity) as it adds final anyRequest matcher
//        http.authorizeRequests().
//            .antMatchers("/public/**")
//            .permitAll();

        super.configure(http);

        // This is important to register your login view to the
        // view access checker mechanism:
        setLoginView(http, LoginView.class);
    }

    /**
     * The password encoder to use when encrypting passwords.
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web)
        throws Exception
    {
        // Customize your WebSecurity configuration.
        super.configure(web);
    }
}
