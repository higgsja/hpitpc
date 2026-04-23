package com.hpi.tpc.ui.views.tools.hedge;

import com.hpi.tpc.services.TPCDAOImpl;
import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Component
//@CssImport(value = "./styles/hedge-grid.css",
//    id = "hedge-grid", themeFor = "vaadin-grid")
@NoArgsConstructor
public class HedgePortfolioOutputs
{

    @Autowired TPCDAOImpl serviceTPC;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired HedgePortfolioInputs inputs;

    private final Grid<HedgeOptionVerticalModel> hedgeOptionsGrid = new Grid<>();
    @Getter private List<HedgeOptionVerticalModel> hedgeOptionVerticalModels;
    @Getter private VerticalLayout outputsLayout;

    @PostConstruct
    public void init()
    {
        this.doHedgeOutputsLayout();
    }

    private void doHedgeOutputsLayout()
    {
        this.outputsLayout = new VerticalLayout();
        this.outputsLayout.addClassName("outputsVL2");
        this.outputsLayout.setSizeFull();

        this.hedgeOptionsGrid.addClassName("hedgeGrid");
        this.hedgeOptionsGrid.setId("hedge-grid");
        this.hedgeOptionsGrid.setThemeName("dense");

        this.hedgeOptionsGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        this.hedgeOptionsGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.hedgeOptionsGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.hedgeOptionsGrid.setHeightFull();
        this.hedgeOptionsGrid.setSelectionMode(Grid.SelectionMode.NONE);

        this.hedgeOptionsGrid.addColumn(HedgeOptionVerticalModel::getBuyStrike)
            .setHeader("Buy")
            //.setWidth("70px")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setKey("buystrike");

        this.hedgeOptionsGrid.addColumn(HedgeOptionVerticalModel::getSellStrike)
            .setHeader("Sell")
            //.setWidth("70px")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setKey("sellstrike");

        this.hedgeOptionsGrid.addColumn(HedgeOptionVerticalModel::getSpreadCost)
            .setHeader("Spread")
            .setTextAlign(ColumnTextAlign.END)
            //.setWidth("90px")
            .setAutoWidth(true);

        this.hedgeOptionsGrid.addColumn(
            HedgeOptionVerticalModel::getCalcMultiple)
            .setHeader("CalcMult")
            .setTextAlign(ColumnTextAlign.END)
            //.setWidth("100px")
            .setAutoWidth(true);

        this.hedgeOptionsGrid.
            addColumn(HedgeOptionVerticalModel::getRealizedMultiple)
            .setHeader("RealMult")
            .setTextAlign(ColumnTextAlign.END)
            .setClassNameGenerator(hedgeOptionVerticalModel -> Double
            .parseDouble(hedgeOptionVerticalModel.
                getRealizedMultiple()) > 10.0 ? "grid-cell-grn" : "")
            //.setWidth("100px")
            .setAutoWidth(true);

        this.hedgeOptionsGrid.addColumn(HedgeOptionVerticalModel::getContracts)
            .setHeader("Ctrcts")
            //.setWidth("80px")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END);

        this.hedgeOptionsGrid.addColumn(HedgeOptionVerticalModel::getBreakEven)
            .setHeader("BrkEvn")
            //.setWidth("90px")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setKey("gainpct")
            .setTextAlign(ColumnTextAlign.END);

        this.hedgeOptionsGrid.addColumn(HedgeOptionVerticalModel::getCost)
            .setHeader("Cost")
            //.setWidth("90px")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END);

        this.hedgeOptionsGrid.setColumnReorderingAllowed(false);
        this.hedgeOptionsGrid.recalculateColumnWidths();
        this.hedgeOptionsGrid.setId("hedge-grid-theme");

        this.outputsLayout.add(this.hedgeOptionsGrid);
    }

    public void doHedgeTable()
    {
        this.hedgeOptionVerticalModels = this.jdbcTemplate.query(String.format(
            HedgeOptionVerticalModel.getSQL(),
            "VXX", "VXX", inputs.getExpirationCB().getValue(), "call",
            inputs.getMinStrikeInput().getValue(),
            inputs.getMaxStrikeInput().getValue()), new HedgeVerticalModelMapper());

        this.doSpreads();

        this.hedgeOptionsGrid.setItems(hedgeOptionVerticalModels);

        this.hedgeOptionsGrid.getDataProvider().refreshAll();
    }

    private void doSpreads()
    {
        HedgeOptionVerticalModel tempVerticalModel;
        ArrayList<HedgeOptionVerticalModel> tempArray;
        Double amount;
        Double buyStrike;
        Double sellStrike;
        Double cost, cost1;
        Double spreadCost;
        Double calcMultiple;
        Double realizedMultiple;
        Double realizationPercent;
        Double calcCost;
        Integer contracts;
        Double breakEven;
        Double ticketCost;
        Double contractCost;
        Double actualCost;
        int i;
        int j;

        // we formatted with comma
        amount = Double.parseDouble(inputs.getAmtInput()
            .getValue().replaceAll(",", ""));

        realizationPercent = Double.parseDouble(inputs.getRealizationInput()
            .getValue()) / 100.0;

        ticketCost = Double.parseDouble(inputs.getTicketCostInput()
            .getValue());

        contractCost = Double.parseDouble(inputs.getContractCostInput()
            .getValue());

        tempArray = new ArrayList<>();

        i = 0; // buy strike
//        j = 0; // sell strike

        for (HedgeOptionVerticalModel vm : this.hedgeOptionVerticalModels)
        {
            j = 0;
            for (HedgeOptionVerticalModel vm1 : this.hedgeOptionVerticalModels)
            {
                // sell strike > buystrike; max 7 wide
                if (j <= i || j > i + 7)
                {
                    // skip
                    j++;
                    continue;
                }

                buyStrike = Double.parseDouble(vm.getBuyStrike());
                sellStrike = Double.parseDouble(vm1.getBuyStrike());
                cost = Double.parseDouble(vm.getCost());
                cost1 = Double.parseDouble(vm1.getCost());
                spreadCost = (cost - cost1) < 0 ? 0.0 : cost - cost1;
                calcMultiple = spreadCost == 0.0 ? 0.0
                    : (sellStrike - buyStrike - spreadCost)
                    / spreadCost;
                realizedMultiple = realizationPercent * calcMultiple;
                calcCost = realizedMultiple == 0.0 ? 0.0
                    : amount / realizedMultiple;
                contracts = spreadCost == 0.0 ? 0
                    : Math.toIntExact(
                        Math.round(calcCost / (spreadCost * 100.0)));
                breakEven = spreadCost == 0.0 ? 0.0 : buyStrike + spreadCost;
                actualCost = spreadCost == 0.0 ? 0.0
                    : (contracts * spreadCost * 100) + (contracts * (2
                    * contractCost)
                    * 2)
                    + (ticketCost * 2);

                tempVerticalModel = new HedgeOptionVerticalModel(
                    //vm.getEquityId(),
                    "",
                    vm.getTicker(),
                    vm.getPutCall(),
                    String.format("%.2f", buyStrike), // buy strike
                    String.format("%.2f", sellStrike), // sell strike
                    String.format("%.2f", spreadCost), // spread cost
                    String.format("%.1f", calcMultiple), // calc Multiple
                    String.format("%.1f", realizedMultiple), // realized multiple
                    contracts.toString(), // contracts
                    String.format("%.2f", breakEven), // breakeven
                    String.format("%.2f", actualCost)); // actual cost

                tempArray.add(tempVerticalModel);

                j++;
            }

            i++;
        }

        this.hedgeOptionVerticalModels = tempArray;
    }
}
