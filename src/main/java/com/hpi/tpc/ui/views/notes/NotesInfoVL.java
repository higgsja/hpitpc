package com.hpi.tpc.ui.views.notes;

import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.notes.NotesConst.*;
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
@Route(value = ROUTE_NOTES_INFO, layout = MainLayout.class)
@PageTitle(TITLE_PAGE_NOTES)
@Component
@PermitAll
public class NotesInfoVL
    extends PageInfoBase
    implements BeforeEnterObserver
{

    @Autowired MainLayout mainLayout;
    @Autowired private TPCDAOImpl noteService;

    @PostConstruct
    public void construct()
    {
        this.init("NotesHelp");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        //log feature use
        this.noteService.AppTracking("TPC:Notes:infoVL");

        //update the gear
        this.mainLayout.updatePagePrefsHL(null);
    }
}
