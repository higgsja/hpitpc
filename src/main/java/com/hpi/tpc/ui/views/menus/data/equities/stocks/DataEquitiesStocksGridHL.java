package com.hpi.tpc.ui.views.menus.data.equities.stocks;

import com.hpi.tpc.ui.views.baseClass.ViewBaseHL;
import static com.helger.commons.string.StringHelper.*;
import com.hpi.tpc.data.entities.*;
import com.vaadin.flow.component.grid.*;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.value.*;
import java.util.*;
import lombok.*;

public class DataEquitiesStocksGridHL
    extends ViewBaseHL
{
    @Getter private Grid<FinVizEquityInfoModel> finVizEquityInfoModelGrid;
    @Getter private FooterRow finVizEquityInfoModelGridFooterRow;

    private HeaderRow gridHeaderRow1;
    private HeaderRow gridHeaderRow2;
    @Getter private final TextField filterTicker;
    @Getter private final TextField filterSector;
    @Getter private final TextField filterIndustry;

    public DataEquitiesStocksGridHL()
    {
        this.addClassName("dataEquitiesStocksGridVL");

        this.finVizEquityInfoModelGrid = null;
        this.finVizEquityInfoModelGridFooterRow = null;
        this.filterTicker = new TextField();
        this.filterSector = new TextField();
        this.filterIndustry = new TextField();

        this.getElement().getStyle().set("padding", "0");
    }

    public final void doLayout(String columnList)
    {
        //completely new grid rather than attempt to adjust
        if (this.finVizEquityInfoModelGrid != null)
        {
            this.remove(this.finVizEquityInfoModelGrid);
            this.filterTicker.setValue("");
            this.filterSector.setValue("");
            this.filterIndustry.setValue("");
        }
        this.finVizEquityInfoModelGrid = new Grid<>();
        this.finVizEquityInfoModelGridFooterRow = this.finVizEquityInfoModelGrid.appendFooterRow();
        this.add(this.finVizEquityInfoModelGrid);

        this.finVizEquityInfoModelGrid.removeAllColumns();

        this.finVizEquityInfoModelGrid.setPageSize(50);
        this.finVizEquityInfoModelGrid.setSizeFull();
        this.finVizEquityInfoModelGrid.addClassName("equity-grid");
        this.finVizEquityInfoModelGrid.setThemeName("dense");
        this.finVizEquityInfoModelGrid.setColumnReorderingAllowed(true);
        this.finVizEquityInfoModelGrid.recalculateColumnWidths();
        this.finVizEquityInfoModelGrid.setAllRowsVisible(false);

        this.setHeaderRow2();

        this.finVizEquityInfoModelGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        this.finVizEquityInfoModelGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        this.finVizEquityInfoModelGrid.setMultiSort(true);

        //set columns based on preferences
        List<String> columns = new ArrayList<>();

        StringTokenizer tokenizer = new StringTokenizer(columnList, ",");
        while (tokenizer.hasMoreElements())
        {
            columns.add(tokenizer.nextToken());
        }

        for (String column : columns)
        {
            switch (trim(column.toLowerCase()))
            {
                case "ticker":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getTicker)
                        .setHeader("Ticker")
                        .setWidth("85px")
                        .setFrozen(true)
                        .setKey("ticker")
                        .setSortProperty("ticker");
                    break;

                case "company":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getCompany)
                        .setHeader("Company")
                        .setWidth("130px")
                        .setResizable(true)
                        .setSortProperty("company");
                    break;

                case "sector":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getSector)
                        .setHeader("Sector")
                        .setWidth("130px")
                        .setKey("sector")
                        .setResizable(true)
                        .setSortProperty("sector");
                    break;

                case "industry":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getIndustry)
                        .setHeader("Industry")
                        .setWidth("130px")
                        .setKey("industry")
                        .setResizable(true)
                        .setSortProperty("industry");
                    break;

                case "country":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getCountry)
                        .setHeader("Country")
                        .setWidth("100px")
                        .setResizable(true)
                        .setSortProperty("country");
                    break;

                case "mktcap(b)":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getMktCap)
                        .setHeader("Cap(B)")
                        .setWidth("80px")
                        .setKey("mktcap")
                        .setSortProperty("mktcap")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "pe":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getPE)
                        .setHeader("PE")
                        .setWidth("70px")
                        .setSortProperty("pe")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "fwdpe":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getFwdPE)
                        .setHeader("PE")
                        .setWidth("70px")
                        .setKey("fwdpe")
                        .setSortProperty("fwdpe")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "peg":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getPEG)
                        .setHeader("PEG")
                        .setWidth("70px")
                        .setSortProperty("peg")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "div":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getDiv)
                        .setHeader("Div")
                        .setWidth("70px")
                        .setSortProperty("div")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "payoutratio":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getPayoutRatio)
                        .setHeader("Out")
                        .setWidth("70px")
                        .setKey("payout")
                        .setSortProperty("payout")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "eps":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getEPS)
                        .setHeader("EPS")
                        .setWidth("70px")
                        .setSortProperty("eps")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "eps/cy":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getEPSCY)
                        .setHeader("CY")
                        .setKey("epscy")
                        .setWidth("70px")
                        .setSortProperty("epscy")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "eps/ny":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getEPSNY)
                        .setHeader("NY")
                        .setKey("epsny")
                        .setWidth("70px")
                        .setSortProperty("epsny")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "eps/p5y":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getEPSP5Y)
                        .setHeader("P5Y")
                        .setKey("epsp5y")
                        .setWidth("70px")
                        .setSortProperty("epsp5y")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "eps/n5y":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getEPSN5Y)
                        .setHeader("N5Y")
                        .setKey("epsn5y")
                        .setWidth("70px")
                        .setSortProperty("epsn5y")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "beta":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getBeta)
                        .setHeader("Beta")
                        .setKey("beta")
                        .setWidth("70px")
                        .setSortProperty("beta")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "atr":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getATR)
                        .setHeader("ATR")
                        .setKey("atr")
                        .setWidth("70px")
                        .setSortProperty("atr")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "sma20":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getSMA20)
                        .setHeader("20D")
                        .setWidth("70px")
                        .setKey("sma20")
                        .setSortProperty("sma20")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "sma50":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getSMA50)
                        .setHeader("50D")
                        .setWidth("70px")
                        .setKey("sma50")
                        .setSortProperty("sma50")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "sma200":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getSMA200)
                        .setHeader("200D")
                        .setWidth("70px")
                        .setKey("sma200")
                        .setSortProperty("sma200")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "50dhi":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getHi50d)
                        .setHeader("HI50d")
                        .setKey("hi50d")
                        .setWidth("80px")
                        .setSortProperty("hi50d")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "50dlo":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getLo50d)
                        .setHeader("Lo50d")
                        .setKey("lo50d")
                        .setWidth("80px")
                        .setSortProperty("lo50d")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "52whi":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getHi52w)
                        .setHeader("Hi52w")
                        .setKey("hi52w")
                        .setWidth("78px")
                        .setSortProperty("hi52w")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "52wlo":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getLo52w)
                        .setHeader("Lo52w")
                        .setKey("lo52w")
                        .setWidth("78px")
                        .setSortProperty("lo52w")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "rsi":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getRSI)
                        .setHeader("RSI")
                        .setKey("rsi")
                        .setWidth("60px")
                        .setSortProperty("rsi")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "anrec":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getAnRec)
                        .setHeader("Rec")
                        .setWidth("70px")
                        .setKey("anrec")
                        .setSortProperty("anrec")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "price":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getPrice)
                        .setHeader("Price")
                        .setKey("price")
                        .setWidth("80px")
                        .setSortProperty("price")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "tgtprice":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getTgtPrice)
                        .setHeader("Price")
                        .setKey("tgtPrice")
                        .setWidth("80px")
                        .setSortProperty("tgtPrice")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "volume":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getVolume)
                        .setHeader("Volume")
                        .setKey("volume")
                        .setWidth("85px")
                        .setSortProperty("volume")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                case "earndate":
                    this.finVizEquityInfoModelGrid.addColumn(FinVizEquityInfoModel::getEarnDate)
                        .setHeader("Date")
                        .setWidth("150px")
                        .setKey("earndate")
                        .setSortProperty("earndate")
                        .setTextAlign(ColumnTextAlign.END);
                    break;

                default:
                    int i = 0;
            }
        }

        this.setColumnFilters();
}

    private void setHeaderRow2()
    {
        this.gridHeaderRow2 = this.finVizEquityInfoModelGrid.prependHeaderRow();
        //todo: if these columns are excluded, they would be null and break here
        if (this.finVizEquityInfoModelGrid.getColumnByKey("mktcap") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("mktcap")).setText("Mkt");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("mktcap") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("mktcap")).setText("Mkt");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("fwdpe") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("fwdpe")).setText("Fwd");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("payout") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("payout")).setText("Pay");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("sma20") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("sma20")).setText("SMA");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("sma50") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("sma50")).setText("SMA");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("sma200") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("sma200")).setText("SMA");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("tgtPrice") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("tgtPrice"))
                .setText("Target");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("anrec") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("anrec")).setText("Anlst");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("earndate") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("earndate"))
                .setText("Earnings");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("epscy") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("epscy")).setText("EPS");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("epsny") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("epsny")).setText("EPS");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("epsp5y") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("epsp5y")).setText("EPS");
        }

        if (this.finVizEquityInfoModelGrid.getColumnByKey("epsn5y") != null)
        {
            this.gridHeaderRow2.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("epsn5y")).setText("EPS");
        }
    }

    private void setColumnFilters()
    {
        //header row        
        this.gridHeaderRow1 = this.finVizEquityInfoModelGrid.appendHeaderRow();

        //ticker column filter
        this.filterTicker.setWidth("95px");
        this.filterTicker.setWidthFull();
        this.filterTicker.setPlaceholder("Filter...");
        this.filterTicker.setClearButtonVisible(true);
        this.filterTicker.setValueChangeMode(ValueChangeMode.TIMEOUT);
        //add filter to header row
        this.gridHeaderRow1.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("ticker"))
            .setComponent(this.filterTicker);

        this.filterSector.setWidth("130px");
        this.filterSector.setWidthFull();
        this.filterSector.setPlaceholder("Filter...");
        this.filterSector.setClearButtonVisible(true);
        this.filterSector.setValueChangeMode(ValueChangeMode.TIMEOUT);
        //add filter to header row
        this.gridHeaderRow1.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("sector"))
            .setComponent(this.filterSector);

        this.filterIndustry.setWidth("130px");
        this.filterIndustry.setWidthFull();
        this.filterIndustry.setPlaceholder("Filter...");
        this.filterIndustry.setClearButtonVisible(true);
        this.filterIndustry.setValueChangeMode(ValueChangeMode.TIMEOUT);
        //add filter to header row
        this.gridHeaderRow1.getCell(this.finVizEquityInfoModelGrid.getColumnByKey("industry"))
            .setComponent(this.filterIndustry);
    }
}
