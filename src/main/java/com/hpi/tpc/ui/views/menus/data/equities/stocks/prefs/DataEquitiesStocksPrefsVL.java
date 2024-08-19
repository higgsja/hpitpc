package com.hpi.tpc.ui.views.menus.data.equities.stocks.prefs;

import com.hpi.tpc.ui.views.baseClass.ViewBaseHL;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.security.*;
import org.springframework.stereotype.Component;

@UIScope
@VaadinSessionScope
@Component
@PermitAll
public class DataEquitiesStocksPrefsVL
    extends ViewBaseHL
{
    public DataEquitiesStocksPrefsVL()
    {
        this.addClassName("dataEquitiesStocksPrefs");
    }
}
