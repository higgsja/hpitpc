package com.hpi.tpc.ui.views.errors;

import com.hpi.tpc.*;
import com.hpi.tpc.ui.exceptions.*;
import com.hpi.tpc.ui.views.main.*;
import jakarta.servlet.http.HttpServletResponse;

import com.vaadin.flow.component.Tag;
//import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.*;
//import com.vaadin.flow.templatemodel.TemplateModel;

/**
 *
 */
@UIScope
@VaadinSessionScope
@Tag("access-denied-view")
@Route(value = "AccessDenied", layout = MainLayout.class)
//@ParentLayout(AppMainLayout.class)
@PageTitle(AppConst.TITLE_PAGE_ACCESS_DENIED)
public class AccessDeniedView
//    extends PolymerTemplate<TemplateModel>
    implements HasErrorParameter<AccessDeniedException> {

    /**
     *
     */
    private static final long serialVersionUID = -49368958204687722L;

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent,
        ErrorParameter<AccessDeniedException> errorParameter) {
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
