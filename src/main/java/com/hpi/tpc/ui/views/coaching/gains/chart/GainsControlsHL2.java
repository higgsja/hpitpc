package com.hpi.tpc.ui.views.coaching.gains.chart;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.coaching.gains.GainsVLModel;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.radiobutton.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Component
public class GainsControlsHL2
    extends ControlsHLBase
{

    @Autowired private GainsVLModel gainsModel;
    @Autowired private JdbcTemplate jdbcTemplate;

    @Getter private final ComboBox<EquityTypeModel> cbEquityType = new ComboBox<>();
    @Getter private final RadioButtonGroup<String> radioButtonGroup = new RadioButtonGroup<>();
    @Getter private final NativeLabel total = new NativeLabel();

    private final HorizontalLayout radioGroupHL = new HorizontalLayout();
    private final HorizontalLayout totalHL = new HorizontalLayout();

    public GainsControlsHL2()
    {
        this.addClassName("gainsControlsHL2");
    }

    /**
     *
     */
    @Override
    public void doLayout()
    {
        this.setupEquityTypes();
        this.doRadioGroupHL();
        this.doTotalHL();

        this.add(this.cbEquityType, this.radioGroupHL, this.totalHL);
    }

    private void doRadioGroupHL()
    {
        this.radioButtonGroup.setItems("%", "$");
        this.radioButtonGroup.setClassName("radioGroup");
        this.radioButtonGroup.setValue(this.gainsModel.getSelectedChartType());

        this.radioGroupHL.add(this.radioButtonGroup);
    }

    private void doTotalHL()
    {
//        this.total.setText("Total: $999,999.99");
        this.total.setClassName("coachingGainsTotal");
        
        this.total.getStyle().set("margin", "0px");
        this.total.getStyle().set("padding-top", "6px");

        this.totalHL.add(this.total);
    }

    private void setupEquityTypes()
    {
        this.cbEquityType.setWidth("110px");

        this.setEquityTypes((this.gainsModel.getSelectedTimeframeModel().getTimeframe().equalsIgnoreCase("Open")
            ? "PositionsOpen" : "PositionsClosed"), true);
    }

    protected void setEquityTypes(String dataTable, Boolean bInit)
    {
        List<EquityTypeModel> equityTypes;
        List<EquityTypeModel> tmpEquityTypes;

        equityTypes = new ArrayList<>();
        //add the All choice
        equityTypes.add(EquityTypeModel.builder()
            .equityType("--All--")
            .build());

        tmpEquityTypes = jdbcTemplate.query(
            String.format(EquityTypeModel.SQL_DISTINCT_EQUITY_TYPES,
                dataTable,
                this.gainsModel.getSelectedPositionModel().getTicker(),
                this.gainsModel.getSelectedPositionModel().getTicker(),
                this.gainsModel.getSelectedTradeTacticModel().getTacticId(),
                this.gainsModel.getSelectedTradeTacticModel().getTacticId(),
                this.gainsModel.getSqlTimeframe(),
                SecurityUtils.getUserId()), //all equityType
            new EquityTypeMapper());

        //put the rest of equityTypes into the array
        for (EquityTypeModel etm : tmpEquityTypes)
        {
            //uppercase first letter
            etm.setEquityType(etm.getEquityType().substring(0, 1).toUpperCase()
                + etm.getEquityType().substring(1).toLowerCase());
            equityTypes.add(etm);
        }

        if (bInit)
        {
            this.gainsModel.setEquityTypeModels(equityTypes);

            this.cbEquityType.setItems(this.gainsModel.getEquityTypeModels());
            if (!this.gainsModel.getEquityTypeModels().isEmpty())
            {
                this.cbEquityType.setValue(this.gainsModel.getEquityTypeModels().get(0));
            }
        } else
        {
            //odd behavior in combobox if we switch to a new list that is the same as the old list
            //  do not do that (Doc, it hurts when I do that ...)
            EquityTypeModel[] array1 = equityTypes.toArray(new EquityTypeModel[0]);
            EquityTypeModel[] array2 = this.gainsModel.getEquityTypeModels().toArray(new EquityTypeModel[0]);
            if (!Arrays.equals(array1, array2))
            {
                this.gainsModel.setEquityTypeModels(equityTypes);
                //change pick list
                this.cbEquityType.setItems(this.gainsModel.getEquityTypeModels());
                this.cbEquityType.setValue(this.gainsModel.getSelectedEquityTypeModel());
            }
        }
    }
}
