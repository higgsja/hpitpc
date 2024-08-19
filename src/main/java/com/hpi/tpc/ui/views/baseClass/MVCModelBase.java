package com.hpi.tpc.ui.views.baseClass;

import com.hpi.tpc.prefs.*;
import com.hpi.tpc.services.*;
import com.vaadin.flow.spring.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@UIScope
@VaadinSessionScope
@Component
@NoArgsConstructor
abstract public class MVCModelBase
{

    @Autowired public TPCDAOImpl serviceTPC;
    @Autowired public PrefsController prefsController;
    @Autowired public JdbcTemplate jdbcTemplate;

    abstract public void getPrefs(String prefix);

    abstract public void writePrefs(String prefix);
}
