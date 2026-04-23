package com.hpi.tpc.ui.views.setup;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@Route(value = ROUTE_SETUP_PREFERENCES, layout = MainLayout.class)
@org.springframework.stereotype.Component
@NoArgsConstructor
@PageTitle(TITLE_PAGE_SETUP)
@PermitAll
public class SetupPrefs
    extends PageInfoBase
    implements BeforeEnterObserver
{
    
    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        this.init("SetupHelp");
    }
    
}
