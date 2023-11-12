package com.hpi.tpc.ui.views.portfolio.plan.charts;

import com.hpi.tpc.ui.views.portfolio.plan.sectors.PlanSectorsMVC_M;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.main.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import java.util.stream.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
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
public class PlanChartsMVC_M {
    @Autowired private MainLayout mainLayout;
    @Autowired private PlanSectorsMVC_M planSectorsMVCModel;
    @Autowired private TPCDAOImpl serviceTPC;

    @Getter private Double[] doublesTargetValues;
    @Getter private String[] stringsTargetLabels;
    @Getter private Double[] doublesActualValues;
    @Getter private String[] stringsActualLabels;

    private final List<String> targetLabels = new ArrayList<>();
    private final List<Double> targetValues = new ArrayList<>();
    private final List<String> actualLabels = new ArrayList<>();
    private final List<Double> actualValues = new ArrayList<>();

    public PlanChartsMVC_M() {
        int i = 0;
    }

    public void doPieData() {
        Iterator itemsIterator;
        ClientSectorModel clientSectorModel;

        this.targetLabels.clear();
        this.targetValues.clear();
        this.actualLabels.clear();
        this.actualValues.clear();

        //do not want a filtered view for the charts so use dataProvider
        Query<ClientSectorModel, Void> query;
        Stream<ClientSectorModel> streamClientSectorModels;

        streamClientSectorModels = serviceTPC.getClientSectorModels(ClientSectorModel
            .SQL_SELECT_ALL_SECTORS_BY_TGTPCT_D).stream();

        itemsIterator = this.mainLayout.getClientAllSectorListModels().stream().iterator();

        while (itemsIterator.hasNext()) {
            clientSectorModel = (ClientSectorModel) itemsIterator.next();

            this.targetLabels.add(clientSectorModel.getCSecShort());
            this.targetValues.add(clientSectorModel.getTgtPct());

            this.actualLabels.add(clientSectorModel.getCSecShort());
            this.actualValues.add(clientSectorModel.getActPct());
        }

        this.doublesTargetValues = this.targetValues.toArray(new Double[0]);
        this.stringsTargetLabels = this.targetLabels.toArray(new String[0]);

        this.doublesActualValues = this.actualValues.toArray(new Double[0]);
        this.stringsActualLabels = this.actualLabels.toArray(new String[0]);
    }
}
