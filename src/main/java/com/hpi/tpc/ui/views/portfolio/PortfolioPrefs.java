package com.hpi.tpc.ui.views.portfolio;

import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@Route(value = ROUTE_PORTFOLIO_PREFERENCES, layout = MainLayout.class)
@org.springframework.stereotype.Component
@NoArgsConstructor
@PageTitle(TITLE_PORTFOLIO_PREFERENCES)
@PermitAll
public class PortfolioPrefs
    extends PageInfoBase
    implements BeforeEnterObserver
{

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        this.init("PortfolioHelp");
    }
}
