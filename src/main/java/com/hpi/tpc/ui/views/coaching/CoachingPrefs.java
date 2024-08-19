package com.hpi.tpc.ui.views.coaching;

import com.hpi.tpc.ui.views.baseClass.*;
import static com.hpi.tpc.ui.views.coaching.CoachingConst.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@Route(value = ROUTE_COACHING_PREFERENCES, layout = MainLayout.class)

@NoArgsConstructor
@PageTitle(TITLE_PAGE_COACHING)
@PermitAll
public class CoachingPrefs
    extends PageInfoBase
    implements BeforeEnterObserver
{

    @Override
    public void beforeEnter(BeforeEnterEvent bee)
    {
        this.init("CoachingHelp");
    }
}
