package com.hpi.tpc.ui.views.coaching.gains;

import com.hpi.tpc.ui.views.baseClass.*;
import static com.hpi.tpc.ui.views.coaching.CoachingConst.*;
import com.hpi.tpc.ui.views.main.*;
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
@Route(value = ROUTE_COACHING_GAINS_PREFS, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_COACHING)
@Component
@PermitAll
public class GainsPrefsVL
    extends PageInfoBase
    implements BeforeEnterObserver
{

    @Autowired MainLayout mainLayout;

    @PostConstruct
    public void construct()
    {
        this.init("CoachingHelp");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        //update the gear
        this.mainLayout.updatePagePrefsHL(null);
    }
}
