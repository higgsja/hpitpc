package com.hpi.tpc.ui.views.menus.data.equities.stocks;

import com.hpi.tpc.ui.views.menus.data.Attribute;
import com.flowingcode.vaadin.addons.twincolgrid.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.services.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.data.provider.*;
import java.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.support.rowset.*;
import org.springframework.stereotype.*;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@Component
public class DataEquitiesStocksModel
    extends MVCModelBase
{

    @Autowired private FinVizEquityInfoModelService equityInfoService;

    @Getter private String stringColumns;
    @Getter private final List<Attribute> availableAttributes;
    @Getter private final HashSet<Attribute> selectedAttributes;
    @Getter private FilterFinVizInfo gridFilter;
    @Getter private ConfigurableFilterDataProvider<FinVizEquityInfoModel, Void, FilterFinVizInfo> dataProvider;
    private final String prefsPrefix = "DataEquitiesStocks";

    public DataEquitiesStocksModel()
    {
        this.availableAttributes = new ArrayList<>();

        this.selectedAttributes = new LinkedHashSet<>();

        this.dataProvider = null;
    }

    @Override
    public void getPrefs(String prefs)
    {
        this.prefsController.readPrefsByPrefix(prefs);
        this.stringColumns = this.prefsController.getPref(this.prefsPrefix + "Columns");
    }

    public void writePrefs(TwinColGrid<Attribute> twinColGrid)
    {
        //todo: issue with twinColGrid
//        int i = 0;
        this.stringColumns = "";

        twinColGrid.getSelectionGrid().getListDataView().getItems().forEachOrdered(
            attribute ->
        {
            this.stringColumns += attribute + ", ";
        }
        );

        this.stringColumns = this.stringColumns.substring(0, this.stringColumns.length() - 2);

        this.prefsController.setPref(this.prefsPrefix + "Columns", this.stringColumns);

        this.prefsController.writePrefsByPrefix(this.prefsPrefix + "Columns");
    }

    public void initAttributeData()
    {
        SqlRowSet rs;
        String columnsCSV;
        StringTokenizer tokenizer;
        Attribute tempAttribute;

        columnsCSV = null;
        this.selectedAttributes.clear();
        this.availableAttributes.clear();

        //populate selected
        tokenizer = new StringTokenizer(this.stringColumns, ",");
        while (tokenizer.hasMoreElements())
        {
            this.selectedAttributes.add(new Attribute(tokenizer.nextToken().trim()));
        }

        //get full list of attributes without selected
        String sql = "select KeyValue from hlhtxc5_dmOfx.TPCPreferences where JoomlaId = 0 and KeyId = 'DataEquitiesStocksColumns';";
        rs = jdbcTemplate.queryForRowSet(sql);

        while (rs.next())
        {
            //only one row
            columnsCSV = rs.getString("KeyValue");
        }

        tokenizer = new StringTokenizer(columnsCSV, ",");

        //add to available only if not already in selected
        while (tokenizer.hasMoreElements())
        {
            tempAttribute = new Attribute(tokenizer.nextToken().trim());

            if (!this.selectedAttributes.contains(tempAttribute))
            {
                this.availableAttributes.add(tempAttribute);
            }
        }

        //sort available
        this.availableAttributes.sort(Comparator.comparing(Attribute::getAttribute));
    }

    public void finVizEquityInfoModelGridDataProviderSetup()
    {
        //todo: likely does not take a ConfigurableFilterDataProvider
        //we are actually filtering in the query
        //this means get a new empty filter
        this.gridFilter = new FilterFinVizInfo();

        DataProvider<FinVizEquityInfoModel, FilterFinVizInfo> dp = DataProvider.fromFilteringCallbacks(
            query ->
        {
            return this.equityInfoService.findAll(query.getOffset(),
                query.getLimit(), query.getFilter());
        },
            query ->
        {
            return this.equityInfoService.findAllCount(query.getOffset(),
                query.getLimit(), query.getFilter());
        });

        this.dataProvider = dp.withConfigurableFilter();

        this.dataProvider.setFilter(gridFilter);
    }

    @Override
    public void writePrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
