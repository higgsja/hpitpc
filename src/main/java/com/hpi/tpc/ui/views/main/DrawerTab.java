package com.hpi.tpc.ui.views.main;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.tabs.*;
import lombok.*;

public class DrawerTab
    extends Tab
{

    @Getter
    private final String navTarget;

    public DrawerTab(String navTarget, Component... components)
    {
        super(components);
        this.navTarget = navTarget;
    }
}
