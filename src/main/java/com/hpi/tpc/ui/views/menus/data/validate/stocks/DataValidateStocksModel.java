package com.hpi.tpc.ui.views.menus.data.validate.stocks;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.data.provider.*;
import java.util.*;
import lombok.*;
import org.springframework.stereotype.*;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@Component
public class DataValidateStocksModel
    extends MVCModelBase
{

    @Getter private List<EditAccountModel> accountModels;
    @Getter private List<TickerModel> tickerModels;

    @Getter private ListDataProvider<ValidateStockTransactionModel> gridDataProvider;
    @Getter private final List<ValidateStockTransactionModel> dbList;

    @Getter @Setter private EditAccountModel selectedAccountModel;
    @Getter @Setter private TickerModel selectedTickerModel;
    @Getter @Setter private Boolean selectedSkip = false;
    @Getter @Setter private Boolean selectedValidated = false;

    /**
     * track when setting a filter. Do not want to enable save if only
     * a filter change on the data provider
     */
    @Getter private Boolean bInFilterChange = false;

    public DataValidateStocksModel()
    {
        this.dbList = new ArrayList<>();
    }

    public void updateAccountModels()
    {
        this.accountModels = this.serviceTPC.getAccountModels();
        
        //set nothing selected
        this.selectedAccountModel = null;
    }

    public void updateTickerModels()
    {
        this.tickerModels = this.serviceTPC.getTickerModels(this.selectedAccountModel);
        //set nothing selected
        this.selectedTickerModel = null;
    }

    /**
     * retrieves data for the grid
     */
    public void updateGridData()
    {
        List<ValidateStockTransactionModel> aList;
        ValidateStockTransactionModel tmpVstm;

        this.dbList.clear();

        //refresh grid data
        aList = serviceTPC.getValidateStockTransactionModels(
            this.selectedAccountModel, this.selectedTickerModel);
        for (ValidateStockTransactionModel model : aList)
        {
            tmpVstm = ValidateStockTransactionModel.builder()
                .joomlaId(model.getJoomlaId())
                .dbAcctId(model.getDbAcctId())
                .equityId(model.getEquityId())
                .ticker(model.getTicker())
                .secName(model.getSecName())
                .lastPrice(model.getLastPrice())
                .dtAsOf(model.getDtAsOf())
                .units(model.getUnits())
                .tradePrice(model.getTradePrice())
                .markUpDn(model.getMarkUpDn())
                .commission(model.getCommission())
                .taxes(model.getTaxes())
                .fees(model.getFees())
                .total(model.getTotal())
                .brokerId(model.getBrokerId())
                .dtTrade(model.getDtTrade())
                .fiTId(model.getFiTId())
                .transactionType(model.getTransactionType())
                .skip(model.getSkip())
                .bSkip(model.getBSkip())
                .validated(model.getValidated())
                .bValidated(model.getBValidated())
                .complete(model.getComplete())
                .bComplete(model.getBComplete())
                .build();
            this.dbList.add(tmpVstm);
        }

        this.gridDataProvider = new ListDataProvider<>(aList);
        this.gridDataProvider.refreshAll();
    }

    /**
     * Set data filters on data set
     *
     * @param skipBoolean: true to view only Skip records
     * @param validatedBoolean: true to view only Validated records
     */
    public void filterChange(Boolean skipBoolean, Boolean validatedBoolean)
    {
        if (this.gridDataProvider == null)
        {
            return;
        }
        this.bInFilterChange = true;

        this.gridDataProvider.clearFilters();
        this.selectedSkip = skipBoolean != null
            ? skipBoolean : this.selectedSkip;
        this.selectedValidated = validatedBoolean != null
            ? validatedBoolean : this.selectedValidated;

        if (this.selectedSkip)
        {
            this.gridDataProvider.addFilter(transaction
                -> Objects.equals(transaction.getBSkip(), this.selectedSkip));
        }

        if (this.selectedValidated)
        {
            this.gridDataProvider.addFilter(transaction
                -> Objects.equals(transaction.getBValidated(),
                    this.selectedValidated));
        }
        this.bInFilterChange = false;
    }

    /**
     * currently not called as there are none
     * @param prefs: Preferences prefix
     */
    public void getPrefs(String prefs)
    {
        this.prefsController.readPrefsByPrefix(prefs);
        if (this.prefsController.getPref("ValidateValidateSkip") != null)
        {
            this.selectedSkip = this.prefsController
                .getPref("StockValidateSkip").equals("Yes");
        }
        if (this.prefsController.getPref("ValidateSkip") != null)
        {
            this.selectedValidated = this.prefsController
                .getPref("StockValidateValidated").equals("Yes");
        }
    }

    /**
     * currently not called as there are no preferences
     * @param skip
     * @param validated 
     */
    public void writePrefs(String skip, String validated)
    {
        //preferences, update the hashmap, then write to database
        this.prefsController.setPref("StockValidateSkip", skip);
        this.prefsController.setPref("StockValidateValidated", validated);
        this.prefsController.writePrefsByPrefix("StockValidate");
    }

    void doSave()
    {
        Integer i;
        Iterator dataProviderIterator;
        ValidateStockTransactionModel tmpModel;

        //Allowing change to Skip and Valid:
        //  if Skip, also Valid
        // will it be in the same order? So far. Is it guaranteed to be so? dunno

        /*
         * compare dbVotModels to grid dataProvider, save as required
         * assumptions:
         * delete is not allowed: both arrays are the same
         * only change allowed is to the Skip and/or Validate field
         */
        i = 0;

        dataProviderIterator = this.gridDataProvider.getItems().iterator();

        while (dataProviderIterator.hasNext())
        {
            tmpModel = (ValidateStockTransactionModel) dataProviderIterator.next();
            if (tmpModel.getBSkip().equals(this.dbList.get(i).getBSkip()))
            {
                //no change
            } else
            {
                //new skip
                //write to database
                this.writeTransaction(
                    tmpModel.getBSkip(),
                    tmpModel.getBValidated(),
                    tmpModel.getDbAcctId(),
                    tmpModel.getFiTId());
            }

            if (tmpModel.getBValidated().equals(this.dbList.get(i).getBValidated()))
            {
                //no change
            } else
            {
                //new validated
                //write to database
                this.writeTransaction(
                    tmpModel.getBSkip(),
                    tmpModel.getBValidated(),
                    tmpModel.getDbAcctId(),
                    tmpModel.getFiTId());
            }
            i++;
        }
    }

    private void writeTransaction(Boolean skip, Boolean validated,
        Integer acctId, String fiTId)
    {
        String sql;

        sql = String.format(ValidateStockTransactionModel.SQL_UPDATE_INVTRAN,
            skip ? 1 : 0, validated ? 1 : 0, acctId, fiTId);

        this.serviceTPC.executeSQL(sql);
    }

    @Override
    public void writePrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
