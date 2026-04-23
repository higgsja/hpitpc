package com.hpi.tpc.ui.views.baseClass;

import com.hpi.tpc.services.TPCDAOImpl;
import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.data.provider.*;
import com.vaadin.flow.data.renderer.*;
import com.vaadin.flow.shared.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
@Getter @Setter
//@CssImport(value = "./styles/tpc-grid-theme.css", id = "position-grid", themeFor = "vaadin-grid")
public class PositionGridVLBase
    extends VerticalLayout
{

    @Autowired private TPCDAOImpl serviceTPC;
    @Autowired private JdbcTemplate jdbcTemplate;

    private final ArrayList<Registration> listeners = new ArrayList<>();
    private final Grid<PositionModel> positionGrid = new Grid<>();
    private ListDataProvider<PositionModel> dataProvider;

    public PositionGridVLBase()
    {
        this.setClassName("positionGridVLBase");
    }

    @PostConstruct
    private void init()
    {
//        this.setSizeFull();
        this.setMinWidth("360px");
        this.setWidth("850px");
        this.setMaxWidth("850px");
        this.doPositionGrid();
        this.doExpandableRowsPositions();
    }

    @PreDestroy
    private void destruct()
    {
        listeners.forEach(listener ->
        {
            listener.remove();
        });
    }

    private void doPositionGrid()
    {
        this.positionGrid.setId("positionsgridVL");
        this.positionGrid.setThemeName("dense");

        this.positionGrid.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        this.positionGrid.addThemeVariants(GridVariant.LUMO_COMPACT);
        this.positionGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.positionGrid.setSelectionMode(Grid.SelectionMode.SINGLE);

        this.positionGrid.setAllRowsVisible(false);
        this.positionGrid.setMinWidth("360px");
        this.positionGrid.setMaxWidth("860px");

        this.positionGrid.addColumn(PositionModel::getPositionName)
            .setHeader("Position")
            .setWidth("90px")
            .setResizable(true)
//            .setFlexGrow(0)
            .setFrozen(true)
            .setKey("positionName")
            //when there is a close date, color the cell to indicate closed
            .setClassNameGenerator(model -> model.getDateClose() != null ? "grid-cell-clsd" : "")
            .setSortProperty("equityId");

        //for open, Date is open date
        //for close, Date is close date
        this.positionGrid.addColumn(PositionModel::getDate)
            .setHeader("Date")
            .setWidth("80px")
            .setKey("date")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setFlexGrow(0)
            //when there is a close date, color the cell to indicate closed
            .setClassNameGenerator(model -> model.getDateClose() != null ? "grid-cell-clsd" : "")
            .setSortProperty("date");

        this.positionGrid.addColumn(PositionModel::getUnits)
            .setHeader("Units")
            .setKey("units")
            .setWidth("80px")
            .setTextAlign(ColumnTextAlign.END)
            .setFlexGrow(0)
            //when there is a close date, color the cell to indicate closed
            .setClassNameGenerator(model -> model.getDateClose() != null ? "grid-cell-clsd" : "")
            .setSortProperty("units");

        this.positionGrid.addColumn(new NumberRenderer<>(
            PositionModel::getPriceOpen,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Open$")
            .setWidth("80px")
            .setKey("iPrice")
            .setTextAlign(ColumnTextAlign.END)
            .setFlexGrow(0)
            //when there is a close date, color the cell to indicate closed
            .setClassNameGenerator(model -> model.getDateClose() != null ? "grid-cell-clsd" : "")
            .setSortProperty("iPrice");

        this.positionGrid.addColumn(new NumberRenderer<>(
            PositionModel::getPrice,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Curr$")
            .setWidth("80px")
            .setKey("priceOpen")
            .setTextAlign(ColumnTextAlign.END)
            .setFlexGrow(0)
            //when there is a close date, color the cell to indicate closed
            .setClassNameGenerator(model -> model.getDateClose() != null ? "grid-cell-clsd" : "")
            .setSortProperty("priceOpen");

        this.positionGrid.addColumn(PositionModel::getDays)
            .setHeader("Days")
            .setWidth("80px")
            .setKey("days")
            .setTextAlign(ColumnTextAlign.END)
            .setFlexGrow(0)
            .setClassNameGenerator(model -> model.getPositionName()
            //when there is a close date, color the cell to indicate closed
            .equalsIgnoreCase(model.getTicker()) ? ""
            : model.getDateClose() != null
            ? "grid-cell-clsd" : model
                //when days to expiry is <= 15 highlight the cell
                .getDays() <= 15 ? "grid-cell-neg" : model.getDays() <= 30
                ? "grid-cell-yel" : "")
            .setSortProperty("days");

        this.positionGrid.addColumn(new NumberRenderer<>(
            PositionModel::getGainPct,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain%")
            .setWidth("85px")
            .setKey("gainPct")
            .setTextAlign(ColumnTextAlign.END)
            .setFlexGrow(0)
            //highlight gainPct cells based on percent above or below 0
            .setClassNameGenerator(model
                -> //>+-2%, >+-5%
                model.getGainPct() < -2.0 && model.getGainPct() > -5.0
            ? "grid-cell-yel"
            : model.getGainPct() <= -5.0
            ? "grid-cell-neg"
            : model.getGainPct() > 5.0
            ? "grid-cell-grn"
            : "")
            .setSortProperty("gainPct");

        this.positionGrid.addColumn(new NumberRenderer<>(
            PositionModel::getGain,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain")
            .setWidth("80px")
            .setKey("gain")
            .setTextAlign(ColumnTextAlign.END)
            .setFlexGrow(0)
            .setSortProperty("gain");

        this.positionGrid.setColumnReorderingAllowed(true);
        this.positionGrid.recalculateColumnWidths();

        this.add(this.positionGrid);
    }

    /**
     * handle expandable rows for open and closed positions by exposing
     * the positionOpen/CloseTransactionModel elements
     */
    private void doExpandableRowsPositions()
    {
        //disable auto-open/close on click so expanded stay open after opened
        this.positionGrid.setDetailsVisibleOnClick(false);

        //add click that toggles expansion on click
        this.listeners.add(this.positionGrid.addItemClickListener((ItemClickEvent<PositionModel> event) ->
        {
            if (event.getItem() != null)
            {
                final PositionModel positionModel = event.getItem();
                positionGrid.setDetailsVisible(positionModel, !positionGrid.isDetailsVisible(positionModel));
            }
        }));

        this.positionGrid.setItemDetailsRenderer(
            new ComponentRenderer<>(positionModel ->
            {
                VerticalLayout div;
                List<PositionClosedTransactionModel1> positionClosedTransactionModel1s;
                List<FIFOClosedTransactionModel1> fifoClosedTransactionModel1s;
                List<FIFOOpenTransactionModel1> fifoOpenTransactionModel1s;
                List<PositionOpenTransactionModel1> positionOpenTransactionModel1s;

//                positionClosedTransactionModel1s = new ArrayList<>();
//                fifoOpenTransactionModel1s = new ArrayList<>();
//                positionOpenTransactionModel1s = new ArrayList<>();

                div = new VerticalLayout();

                if (positionModel.getDateClose() == null)
                {
                    //an open position
                    if (positionModel.getEquityType().equalsIgnoreCase("stock"))
                    {
                        //open stock 
//                        positionOpenTransactionModel1s = jdbcTemplate.query(String
//                            .format(PositionOpenTransactionModel1.GET_STOCK_BY_POSITIONID_JOOMLAID,
//                                positionModel.getPositionId(),
//                                SecurityUtils.getUserId()),
//                            new PositionOpenTransactionMapper1());
                        //really want to show fifoOpenTransactions for stocks
                        fifoOpenTransactionModel1s = jdbcTemplate.query(
                            String.format(FIFOOpenTransactionModel1.GET_STOCK_BY_TICKER_DATEOPEN_JOOMLAID,
                                positionModel.getTicker(),
                                SecurityUtils.getUserId()),
                            new FIFOOpenTransactionMapper1());
                        
                        Grid<FIFOOpenTransactionModel1> details
                            = this.doOpenExpandedGridFOTM(fifoOpenTransactionModel1s);
                        div.add(details);
                    }

                    if (positionModel.getEquityType().equalsIgnoreCase("option"))
                    {
                        //open option
                        positionOpenTransactionModel1s = jdbcTemplate.query(String
                            .format(PositionOpenTransactionModel1.GET_OPTION_BY_POSITIONID_JOOMLAID,
                                positionModel.getPositionId(),
                                SecurityUtils.getUserId()),
                            new PositionOpenTransactionMapper1());

                        Grid<PositionOpenTransactionModel1> details
                            = this.doOpenExpandedGridPOTM(positionOpenTransactionModel1s);
                        div.add(details);
                    }

//                    if (positionOpenTransactionModel1s.isEmpty())
//                    {
//                        return div;
//                    }
//
//                    Grid<PositionOpenTransactionModel1> details
//                        = this.doOpenExpandedGridPOTM(positionOpenTransactionModel1s);
//                    div.add(details);
                    return div;
                } else
                {
                    //closed position
                    if (positionModel.getEquityType().equalsIgnoreCase("stock"))
                    {
                        //closed stock position
                        positionClosedTransactionModel1s = jdbcTemplate.query(
                            String.format(PositionClosedTransactionModel1.GET_BY_POSITIONID_JOOMLAID,
                                positionModel.getPositionId(),
                                SecurityUtils.getUserId()),
                            new PositionClosedTransactionMapper1());
                    } else
                    {
                        //options
                        positionClosedTransactionModel1s = jdbcTemplate.query(String
                            .format(PositionClosedTransactionModel1.GET_BY_POSITIONID_JOOMLAID,
                                positionModel.getPositionId(),
                                SecurityUtils.getUserId()),
                            new PositionClosedTransactionMapper1());
                    }

                    if (positionClosedTransactionModel1s.isEmpty())
                    {
                        return div;
                    }

                    Grid<PositionClosedTransactionModel1> details
                        = this.doClosedExpandedGridPOTM(positionClosedTransactionModel1s);

                    div.add(details);
                    return div;
                }
            }));
    }

    private Grid<PositionOpenTransactionModel1> doOpenExpandedGridPOTM(
        List<PositionOpenTransactionModel1> positionOpenTransactionModel1s)
    {
        Grid<PositionOpenTransactionModel1> gridDetails = new Grid<>();
        gridDetails.setThemeName("dense");

        gridDetails.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        gridDetails.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        if (!positionOpenTransactionModel1s.isEmpty())
        {
            gridDetails.setDataProvider(new ListDataProvider<>(positionOpenTransactionModel1s));
            gridDetails.setAllRowsVisible(true);
        }

        gridDetails.addColumn(PositionOpenTransactionModel1::getTransactionName)
            .setHeader("Transaction")
            //            .setAutoWidth(true)
            .setWidth("80px")
            .setResizable(true)
            .setKey("transactionName");

        gridDetails.addColumn(PositionOpenTransactionModel1::getPositionType)
            .setHeader("Type")
            //.setAutoWidth(true)
            //            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("positionType");

        gridDetails.addColumn(PositionOpenTransactionModel1::getDateOpen)
            .setHeader("OpenDate")
            //.setAutoWidth(true)
            .setWidth("90px")
            .setFlexGrow(0)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("date");

        gridDetails.addColumn(PositionOpenTransactionModel1::getUnits)
            .setHeader("Units")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("units");

        gridDetails.addColumn(new NumberRenderer<>(
            PositionOpenTransactionModel1::getPriceOpen,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Open$")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("iPrice");

        gridDetails.addColumn(PositionOpenTransactionModel1::getDays)
            .setHeader("Days")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("65px")
            .setFlexGrow(0)
            .setKey("days");

        gridDetails.addColumn(new NumberRenderer<>(
            PositionOpenTransactionModel1::getGainPct,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain%")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("75px")
            .setFlexGrow(0)
            .setKey("gainPct");

        gridDetails.addColumn(new NumberRenderer<>(
            PositionOpenTransactionModel1::getGain,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("gain")
            .setSortProperty("gain");

        gridDetails.addColumn(PositionOpenTransactionModel1::getClientAcctName)
            .setHeader("Acct")
            .setAutoWidth(true)
            .setWidth("80px")
            .setKey("acct")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setFlexGrow(0)
            .setSortProperty("acct");

        return gridDetails;
    }
    
    private Grid<FIFOOpenTransactionModel1> doOpenExpandedGridFOTM(
        List<FIFOOpenTransactionModel1> fifoOpenTransactionModel1s)
    {
        Grid<FIFOOpenTransactionModel1> gridDetails = new Grid<>();
        gridDetails.setThemeName("dense");

        gridDetails.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        gridDetails.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        if (!fifoOpenTransactionModel1s.isEmpty())
        {
            gridDetails.setDataProvider(new ListDataProvider<>(fifoOpenTransactionModel1s));
            gridDetails.setAllRowsVisible(true);
        }

        gridDetails.addColumn(FIFOOpenTransactionModel1::getTicker)
            .setHeader("Transaction")
            //            .setAutoWidth(true)
            .setWidth("80px")
            .setResizable(true)
            .setKey("transactionName");

        gridDetails.addColumn(FIFOOpenTransactionModel1::getPositionType)
            .setHeader("Type")
            //.setAutoWidth(true)
            //            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("positionType");

        gridDetails.addColumn(FIFOOpenTransactionModel1::getDateOpen)
            .setHeader("OpenDate")
            //.setAutoWidth(true)
            .setWidth("80px")
            .setFlexGrow(0)
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("date");

        gridDetails.addColumn(FIFOOpenTransactionModel1::getUnits)
            .setHeader("Units")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("units");

        gridDetails.addColumn(new NumberRenderer<>(
            FIFOOpenTransactionModel1::getPriceOpen,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Open$")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("iPrice");

//        gridDetails.addColumn(FIFOOpenTransactionModel1::getDays)
//            .setHeader("Days")
//            //.setAutoWidth(true)
//            .setTextAlign(ColumnTextAlign.END)
//            .setWidth("65px")
//            .setFlexGrow(0)
//            .setKey("days");

        gridDetails.addColumn(new NumberRenderer<>(
            FIFOOpenTransactionModel1::getGainPct,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain%")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("75px")
            .setFlexGrow(0)
            .setKey("gainPct");

        gridDetails.addColumn(new NumberRenderer<>(
            FIFOOpenTransactionModel1::getGain,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain")
            //.setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            .setWidth("80px")
            .setFlexGrow(0)
            .setKey("gain")
            .setSortProperty("gain");

        gridDetails.addColumn(FIFOOpenTransactionModel1::getClientAcctName)
            .setHeader("Acct")
            .setAutoWidth(true)
            .setWidth("80px")
            .setKey("acct")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setFlexGrow(0)
            .setSortProperty("acct");

        return gridDetails;
    }

    private Grid<PositionClosedTransactionModel1> doClosedExpandedGridPOTM(
        List<PositionClosedTransactionModel1> positionClosedTransactionModel1s)
    {
        Grid<PositionClosedTransactionModel1> gridDetails = new Grid<>();
        gridDetails.setThemeName("dense");

        gridDetails.addThemeVariants(GridVariant.LUMO_COLUMN_BORDERS);
        gridDetails.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        if (!positionClosedTransactionModel1s.isEmpty())
        {
            gridDetails.setDataProvider(new ListDataProvider<>(positionClosedTransactionModel1s));
            gridDetails.setAllRowsVisible(true);
        }

        gridDetails.addColumn(PositionClosedTransactionModel1::getTransactionName)
            .setHeader("Transaction Name")
            //.setAutoWidth(true)
            //todo: not the best way to do this
            .setWidth("170px")
            .setKey("transactionName");

        gridDetails.addColumn(PositionClosedTransactionModel1::getPositionType)
            .setHeader("Type")
            .setAutoWidth(true)
            //todo: not the best way to do this
            //.setWidth("70px")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("posType");

        gridDetails.addColumn(PositionClosedTransactionModel1::getDateOpen)
            .setHeader("Open")
            .setAutoWidth(true)
            //.setWidth("85px")
            .setTextAlign(ColumnTextAlign.CENTER)
            .setKey("date");

        gridDetails.addColumn(PositionClosedTransactionModel1::getUnits)
            .setHeader("Units")
            .setAutoWidth(true)
            //.setWidth("85px")
            .setTextAlign(ColumnTextAlign.END)
            .setKey("units");

        gridDetails.addColumn(new NumberRenderer<>(
            PositionClosedTransactionModel1::getPriceOpen,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Open$")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            //.setWidth("80px")
            .setKey("iPrice");

        gridDetails.addColumn(new NumberRenderer<>(
            PositionClosedTransactionModel1::getPriceClose,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Close$")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            //.setWidth("80px")
            .setKey("priceClose");

        gridDetails.addColumn(new NumberRenderer<>(
            PositionClosedTransactionModel1::getGainPct,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain%")
            .setAutoWidth(true)
            .setTextAlign(ColumnTextAlign.END)
            //.setWidth("80px")
            .setKey("gainPct");

        gridDetails.addColumn(new NumberRenderer<>(
            PositionClosedTransactionModel1::getGain,
            "%,.2f",
            Locale.US,
            "0.00"))
            .setHeader("Gain")
            .setAutoWidth(true)
            //.setWidth("80px")
            .setTextAlign(ColumnTextAlign.END)
            .setKey("gain");

        gridDetails.addColumn(PositionClosedTransactionModel1::getClientAcctName)
            .setHeader("Acct")
            .setAutoWidth(true)
            //todo: not the best way to do this
            //.setWidth("70px")
            .setTextAlign(ColumnTextAlign.END)
            .setKey("clientAcct");

        return gridDetails;
    }

    public void getData()
    {
        List<PositionModel> aList;
        String sql;

        //retrieve combined open and closed positions
        sql = String.format(PositionModel.SQL_1YR_ALL_OPEN_CLOSE_POSITIONS,
            SecurityUtils.getUserId().toString(),
            SecurityUtils.getUserId().toString());

        aList = serviceTPC.getJdbcTemplate().query(sql, new PositionMapper1());

        this.dataProvider = new ListDataProvider<>(aList);

        this.positionGrid.setDataProvider(dataProvider);
    }
}