package com.hpi.tpc.ui.views.menus.data;

import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.menus.data.DataConst.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@NoArgsConstructor
@UIScope
@VaadinSessionScope
@Route(value = ROUTE_DATA_INFO, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_DATA)
@Component
@PermitAll
public class DataInfoVL
    extends PageInfoBase
    implements BeforeEnterObserver
{

    @Autowired MainLayout mainLayout;

    @PostConstruct
    public void construct()
    {
        this.init("DataHelp");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        //update the gear
        this.mainLayout.updatePagePrefsHL(null);
    }
}
