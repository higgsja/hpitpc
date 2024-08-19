package com.hpi.tpc;

import com.vaadin.flow.component.page.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.shared.communication.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.*;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Viewport("width=device-width, initial-scale=1")
@PWA(name = "Trading Performance Coach", shortName = "TPC")
@Push(value = PushMode.AUTOMATIC)
public class Application 
    extends SpringBootServletInitializer
    implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
