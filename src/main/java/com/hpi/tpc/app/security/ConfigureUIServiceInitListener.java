package com.hpi.tpc.app.security;

import com.hpi.tpc.ui.views.main.LoginView;
import com.hpi.tpc.ui.components.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.nio.file.*;
import org.springframework.stereotype.Component;

/*
 * Adds before enter listener to check access to views
 * Adds the offline banner
 */
@Component
//VaadinSessionScope
public class ConfigureUIServiceInitListener
    implements VaadinServiceInitListener {

    private static final long serialVersionUID = -2521157571016156491L;

    public ConfigureUIServiceInitListener() {
        super();
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(uiEvent -> {
            final UI ui = uiEvent.getUI();

            ui.add(new OfflineBanner());

            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if not authorized to access the view.
     *
     * @param event
     *              before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event) {
        final boolean accessGranted = SecurityUtils.isAccessGranted(event.getNavigationTarget());
        if (!accessGranted) {
            if (SecurityUtils.isUserLoggedIn()) {
                event.rerouteToError(AccessDeniedException.class);
            }
            else {
                event.rerouteTo(LoginView.class);
            }
        }
    }
}
