package com.hpi.tpc.ui.views.menus.admin;

import static com.hpi.tpc.AppConst.ROUTE_ADMIN_PREFERENCES;
import static com.hpi.tpc.AppConst.TITLE_PAGE_ADMIN;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@Route(value = ROUTE_ADMIN_PREFERENCES, layout = MainLayout.class)

@NoArgsConstructor
@PageTitle(TITLE_PAGE_ADMIN)
@PermitAll
public class AdminPrefs
    extends PageInfoBase
    implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        this.init("AdminHelp");
    }
}
