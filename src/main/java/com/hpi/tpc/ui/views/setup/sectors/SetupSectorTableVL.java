package com.hpi.tpc.ui.views.setup.sectors;

import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import lombok.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
//@CssImport(value = "./styles/setupSectors-grid.css",
//           id = "setupSectors-grid", themeFor = "vaadin-grid")
//@CssImport(value = "./styles/setupSectors.css")
/**
 * Provides the Sectors table
 */
public class SetupSectorTableVL
    extends VerticalLayout {
    @Getter private final Grid<ClientSectorModel> setupSectorsGrid =
        new Grid<>();
    //subset of view needs knowledge of full view
    private SetupSectorsView setupSectorsMVCView;

    public SetupSectorTableVL() {
        this.setClassName("setupSectorsTable");
        this.setSizeFull();
    }

    @PostConstruct
    private void construct() {
        this.doGridSetup();
    }

    @PreDestroy
    private void destruct() {
    }

    private void doGridSetup() {
        this.setupSectorsGrid.setId("setupSectorsTableGrid");
        this.setupSectorsGrid.setThemeName("dense");
        this.setupSectorsGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        this.setupSectorsGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.setupSectorsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.setupSectorsGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.setupSectorsGrid.setHeightFull();
        this.setupSectorsGrid.setAllRowsVisible(true);

        this.setupSectorsGrid.addColumn(ClientSectorModel::getJoomlaId)
            .setHeader("JoomlaId")
            .setWidth("150px")
            .setKey("joomlaid");
        this.setupSectorsGrid.getColumnByKey("joomlaid").setVisible(false);

//        this.setupSectorsGrid.addColumn(ClientSectorModel::getClientSectorId)
//            .setHeader("SectorId")
//            .setWidth("60px")
//            .setKey("clientSectorId");
//        this.setupSectorsGrid.getColumnByKey("clientSectorId").setVisible(false);
        this.setupSectorsGrid.addColumn(ClientSectorModel::getClientSector)
            .setHeader("Sector")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("clientSector")
            .setSortProperty("clientSector");

        this.setupSectorsGrid.addColumn(ClientSectorModel::getCSecShort)
            .setHeader("Short")
            .setAutoWidth(true)
            .setFrozen(true)
            .setKey("cSecShort")
            .setSortProperty("cSecShort");

        this.setupSectorsGrid.addColumn(ClientSectorModel::getComment)
            .setHeader("Comment")
            .setWidth("150px")
            .setResizable(true)
            .setFlexGrow(1)
            .setKey("comment")
            .setSortProperty("comment");

        this.setupSectorsGrid.setColumnReorderingAllowed(true);
        this.setupSectorsGrid.recalculateColumnWidths();

        this.add(this.setupSectorsGrid);
    }

    public void setSetupSectorsMVCView(SetupSectorsView setupSectorsMVCView) {
        this.setupSectorsMVCView = setupSectorsMVCView;
    }
}
