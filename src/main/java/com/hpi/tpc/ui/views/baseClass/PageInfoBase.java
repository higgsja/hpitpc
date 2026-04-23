package com.hpi.tpc.ui.views.baseClass;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.*;

public class PageInfoBase
    extends VerticalLayout
{

    @Autowired private JdbcTemplate jdbcTemplate;

    public PageInfoBase()
    {
        this.setClassName("pagePrefsBase");
    }

    public void init(String keyId)
    {
        Span aSpan;
        String sql;
        String keyValue;

        keyValue = "";

        sql = "select KeyValue from TPCHelp where KeyId = '" + keyId + "'";
        SqlRowSet rowSet = this.jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next())
        {
            //will only be one
            keyValue = rowSet.getString("KeyValue");
        }
        aSpan = new Span();
        aSpan.getElement().setProperty("innerHTML", keyValue);

        this.add(aSpan);
    }
}
