package com.hpi.tpc.ui.views.setup.sectors;

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
public class SetupSectorsModel
extends MVCModelBase{
    @Getter @Setter private ClientSectorModel editClientSectorModel;

    public SetupSectorsModel() {
        int i = 0;
    }

    @PostConstruct
    private void construct() {
        int i = 0;
    }

    public void createDefaultClientSectorModel() {
        this.editClientSectorModel = new ClientSectorModel();
    }

    /**
     * Save edited data
     * Simply write all back to the database
     *
     * @param setupSectorsGrid
     */
    public void doSave(Grid<ClientSectorModel> setupSectorsGrid) {
        //dataProvider has edited data
        String sql;
        Iterator itemsIterator;
        ClientSectorModel csm;

        sql = "";

        itemsIterator = ((ListDataProvider) setupSectorsGrid.getDataProvider()).getItems().iterator();

        //0: standard sector - cannot change ClientSector
        //1: custom sector - can change ClientSector
        while (itemsIterator.hasNext()) {
            csm = (ClientSectorModel) itemsIterator.next();

            if (csm.getChanged().equals(ClientSectorModel.CHANGE_NONE)) {
                //no change, skip
                continue;
            }

            if (csm.getChanged().equals(ClientSectorModel.CHANGE_NEW)) {
                //this is an insert
                sql = ClientSectorModel.SQLINSERT;
                sql += "'" + csm.getJoomlaId() + "', ";
                sql += null + ", ";
                sql += "'" + csm.getClientSector() + "', ";
                sql += "'" + csm.getCSecShort() + "', ";
                sql += "'" + csm.getActive() + "', ";
                sql += "'" + csm.getTgtPct() + "', ";
                sql += "'" + csm.getComment() + "', ";
                sql += "'" + csm.getTgtLocked() + "', ";
                sql += "'" + csm.getActPct() + "', ";
                sql += "'" + csm.getMktVal() + "', ";
                sql += "'" + csm.getLMktVal() + "', ";
                sql += "'" + csm.getCustomSector() + "'";
                sql += ");";

                this.serviceTPC.executeSQL(sql);

                continue;
            }

            //handle edits of existing sectors
            switch (csm.getCustomSector()) {
                case ClientSectorModel.STD_SECTOR: //standard
                    //disallow change of CSecShort
                    sql = String.format(ClientSectorModel.SQL_UPDATE_STD_SECTOR,
                        csm.getClientSector(),
                        csm.getComment(),
                        csm.getJoomlaId(),
                        csm.getClientSectorId());
                    break;
                case ClientSectorModel.CSTM_SECTOR: //custom
                    sql = String.format(ClientSectorModel.SQL_UPDATE_CSTM_SECTOR,
                        csm.getClientSector(),
                        csm.getCSecShort(),
                        csm.getComment(),
                        csm.getJoomlaId(),
                        csm.getClientSectorId());
                    break;
                default:
            }

            this.serviceTPC.executeSQL(sql);
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
