package com.hpi.tpc.ui.views.baseClass;

import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import org.springframework.stereotype.*;

/**
 * works with BaseMVCView2Wide to provide the left vertical layout
 */
@UIScope
@VaadinSessionScope
@Component
@CssImport("./styles/baseMVCViewVL.css")
public class MVCView2WideVLBase
    extends VerticalLayout
{

    public MVCView2WideVLBase()
    {
        this.setClassName("baseMVCViewLeftVL");
        this.setMinWidth("320px");
        this.setWidth("550px");
        this.setHeight("100%");
    }    
}
