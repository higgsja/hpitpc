package com.hpi.tpc.ui.views.main;

import com.hpi.tpc.*;
import static com.hpi.tpc.AppConst.*;
import com.hpi.tpc.app.security.*;
import com.hpi.tpc.ui.views.notes.mine.*;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.login.*;
import com.vaadin.flow.dom.*;
import com.vaadin.flow.router.*;
import com.vaadin.flow.spring.annotation.*;
import com.vaadin.flow.theme.lumo.*;

@UIScope
@VaadinSessionScope
@Route(value = AppConst.LOGON)
@PageTitle(TITLE_PAGE_LOGON)
//@JsModule("./styles/shared-styles.js")
public class LoginView
    extends LoginOverlay
    implements AfterNavigationObserver,
    BeforeEnterObserver
{

    private static final LoginI18n I18N = LoginI18n.createDefault();

    public LoginView()
    {
        I18N.setHeader(new LoginI18n.Header());
        I18N.getHeader().setTitle("Trader Performance Coach");
        I18N.getHeader().setDescription("High Performance Investing");
//        i18n.setAdditionalInformation("shows at the bottom");
        I18N.setForm(new LoginI18n.Form());
        I18N.getForm().setSubmit("Sign in");
        I18N.getForm().setTitle("Sign in");
        I18N.getForm().setUsername("HPI User Name");
        I18N.getForm().setPassword("Password");

        ThemeList themeList = UI.getCurrent().getElement().getThemeList();

        if (themeList.contains(Lumo.DARK))
        {
        } else
        {
            //set dark theme
            themeList.add(Lumo.DARK);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        if (SecurityUtils.isUserLoggedIn())
        {
            //if logged in, go to notes
            event.forwardTo(NotesMineControllerFL.class);
        } else
        {
            //not logged in
            setI18n(I18N);
            setForgotPasswordButtonVisible(false);
            setAction(AppConst.LOGON);
            setOpened(true);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event)
    {
        setError(
            event.getLocation().getQueryParameters().getParameters().
                containsKey(
                    "error"));
    }
}
