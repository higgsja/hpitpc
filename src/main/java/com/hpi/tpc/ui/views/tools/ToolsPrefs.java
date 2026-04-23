package com.hpi.tpc.ui.views.tools;

import static com.hpi.tpc.AppConst.ROUTE_TOOLS_PREFERENCES;
import static com.hpi.tpc.AppConst.TITLE_TOOLS_PREFERENCES;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@Route(value = ROUTE_TOOLS_PREFERENCES, layout = MainLayout.class)
@org.springframework.stereotype.Component
@NoArgsConstructor
//VaadinSessionScope
//@CssImport("./styles/custom.css")
@PageTitle(TITLE_TOOLS_PREFERENCES)
@PermitAll
public class ToolsPrefs
    extends PageInfoBase
    implements BeforeEnterObserver
{

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        this.init("ToolsHelp");
    }
}
