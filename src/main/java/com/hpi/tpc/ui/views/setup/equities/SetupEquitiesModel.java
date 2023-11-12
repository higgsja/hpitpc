package com.hpi.tpc.ui.views.setup.equities;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
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
public class SetupEquitiesModel
    extends MVCModelBase
{

    //holds the original model before going to add/edit
    @Getter @Setter private ClientEquityModel editClientEquityModel;

    public SetupEquitiesModel()
    {
        int i = 0;
    }

    @PostConstruct
    private void construct()
    {
        int i = 0;
    }

    public void createDefaultClientEquityModel()
    {
        this.editClientEquityModel = new ClientEquityModel();
    }

    /**
     * Save edited data
     * Simply write all back to the database
     *
     * @param equityGrid
     */
    public void doSave(Grid<ClientEquityModel> equityGrid)
    {
        //dataProvider has edited data
        String sql;
        Iterator itemsIterator;
        ClientEquityModel cem;

        itemsIterator = ((ListDataProvider) equityGrid.getDataProvider()).getItems().iterator();

        while (itemsIterator.hasNext())
        {
            cem = (ClientEquityModel) itemsIterator.next();

            switch (cem.getChanged())
            {
                case ClientEquityModel.CHANGE_NONE:
                    //do nothing
                    continue;
                case ClientEquityModel.CHANGE_EDIT:
                    //update
                    sql = ClientEquityModel.SQL_UPDATE;
                    sql += "Comment = '" + cem.getComment() + "', ";
                    sql += "ClientSectorId = '" + cem.getClientSectorId() + "'";
                    sql += " where JoomlaId = '" + SecurityUtils.getUserId() + "'";
                    sql += " and Ticker = '" + cem.getTicker() + "';";

                    this.serviceTPC.executeSQL(sql);
                    break;
                case ClientEquityModel.CHANGE_NEW:
                    //insert
                    sql = ClientEquityModel.SQLINSERT;
                    sql += "'" + cem.getJoomlaId() + "', ";
                    sql += "'" + cem.getTicker() + "', ";
                    sql += "'" + cem.getTickerIEX() + "', ";
                    sql += "'" + cem.getActive() + "', ";
                    sql += "'" + cem.getClientSectorId() + "', ";
                    sql += "'" + cem.getTgtPct() + "', ";
                    sql += "'" + cem.getAnalystTgt() + "', ";
                    sql += "'" + cem.getStkPrice() + "', ";
                    sql += "'" + cem.getComment() + "', ";
                    sql += "'" + cem.getTgtLocked() + "', ";
                    sql += "'" + cem.getActPct() + "');";

                    this.serviceTPC.executeSQL(sql);
                    break;
                default:
            }
        }
    }

    @Override
    public void getPrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void writePrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
