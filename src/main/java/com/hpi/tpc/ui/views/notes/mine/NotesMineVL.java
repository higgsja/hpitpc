package com.hpi.tpc.ui.views.notes.mine;

import com.hpi.tpc.ui.views.notes.NotesAbstractVL;
import com.hpi.tpc.ui.views.main.*;
import static com.hpi.tpc.ui.views.notes.NotesConst.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import jakarta.annotation.security.*;
import lombok.*;
import org.springframework.stereotype.Component;

/**
 * makes direct request for data from model
 * does not change data;
 * user actions are sent to the controller
 * dumb, just builds the view
 */
@UIScope
@VaadinSessionScope
@Component
@NoArgsConstructor
@Route(value = ROUTE_NOTES_VIEW_MINE, layout = MainLayout.class)
@PermitAll
@PageTitle(TITLE_PAGE_NOTES + ": " + TITLE_PAGE_NOTES_MINE)
public class NotesMineVL
    extends NotesAbstractVL
    implements BeforeEnterObserver
{
    @PostConstruct
    private void construct()
    {
        //this is hit; super is not unless called
        this.addClassName("notesViewMineVL");
        
        this.doListeners();        
    }
    
     private void doListeners()
    {
        this.getNotesGrid().addItemClickListener(event ->
        {
            this.notesModel.setNoteModel(event.getItem());
            this.notesModel.getBinder().setBean(this.notesModel.getNoteModel());

//            this.notesModel.setIsAdd(false);
            UI.getCurrent().navigate(ROUTE_NOTES_CONTROLLER_EDIT);
        });
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        //there are things needed from the parent
        //super.beforeEnter(event);
    }
}
