package com.hpi.tpc.ui.views.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.tabs.*;
import com.vaadin.flow.spring.annotation.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@NoArgsConstructor
public class MyTab 
    extends Tab
{
    @Getter private String navTargetTitle;

    public MyTab(String navTarget, Component... components)
    {
        super(components);
        this.navTargetTitle = navTarget;
    }
}
