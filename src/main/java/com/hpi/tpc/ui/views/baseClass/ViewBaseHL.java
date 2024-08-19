package com.hpi.tpc.ui.views.baseClass;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;

/**
 * Abstract class for content element on each page
 */
@UIScope
@VaadinSessionScope
public abstract class ViewBaseHL
    extends HorizontalLayout
{

    public ViewBaseHL()
    {
        this.setClassName("viewBaseHL");

        this.setMinWidth("320px");

        this.setSizeFull();
    }

    public final NativeLabel titleFormat(String title)
    {
        NativeLabel label = new NativeLabel(title);
        label.getElement().getStyle().set("font-size", "14px");
        label.getElement().getStyle().set("font-family", "Arial");
//        label.getElement().getStyle().set("color", "#169FF3");
        label.getElement().getStyle().set("margin-top", "0px");
        label.getElement().getStyle().set("margin-bottom", "0px");
        label.getElement().getStyle().set("margin-block-start", "0px");
        label.getElement().getStyle().set("margin-block-end", "0px");
        label.getElement().getStyle().set("line-height", "1em");

        return label;
    }
}
