package com.hpi.tpc.ui.views.main;

import com.hpi.tpc.services.TPCDAOImpl;
import static com.hpi.tpc.AppConst.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.security.*;
import org.springframework.beans.factory.annotation.*;


@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@Route(value = ROUTE_EXIT, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_LOGOUT)
@PermitAll
public class ExitView
    extends VerticalLayout
    implements BeforeEnterObserver {

    @Autowired TPCDAOImpl notesService;

    public ExitView() {

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        notesService.AppTracking("WNotes:Logout");

        VaadinSession.getCurrent().getSession().invalidate();
    }
}