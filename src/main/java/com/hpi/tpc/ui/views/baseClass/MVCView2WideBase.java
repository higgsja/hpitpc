package com.hpi.tpc.ui.views.baseClass;

import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.shared.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;

//@UIScope
//@VaadinSessionScope
//@Component
@CssImport("./styles/baseMVCView2Wide.css")
@Getter
public abstract class MVCView2WideBase
    extends FlexLayout
{

    protected VerticalLayout leftVL;
    protected VerticalLayout rightVL;

    protected final ArrayList<Registration> listeners;

    public MVCView2WideBase()
    {
        this.listeners = new ArrayList<>();
        this.setClassName("baseMVCView2Wide");
        this.setHeight("100%");

        this.leftVL = new MVCView2WideVLBase();
        this.rightVL = new MVCView2WideVLBase();

        this.add(this.leftVL);
        this.add(this.rightVL);
    }

    @PostConstruct
    protected void construct()
    {
        //hit with super
//        int i = 0;
    }

    @PreDestroy
    protected void destruct()
    {
        this.removeListeners();
    }

    public void removeListeners()
    {
        for (Registration r : this.listeners)
        {
            if (r != null)
            {
                r.remove();
            }
        }
    }

    protected NativeLabel titleFormat(String title)
    {
        NativeLabel label = new NativeLabel(title);
        label.getElement().getStyle().set("font-size", "14px");
        label.getElement().getStyle().set("font-family", "Arial");
        label.getElement().getStyle().set("color", "#169FF3");
        label.getElement().getStyle().set("margin-top", "0px");
        label.getElement().getStyle().set("margin-bottom", "0px");
        label.getElement().getStyle().set("margin-block-start", "0px");
        label.getElement().getStyle().set("margin-block-end", "0px");
        label.getElement().getStyle().set("line-height", "1em");

        return label;
    }
}
