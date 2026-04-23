package com.hpi.tpc.ui.views.menus.data.validate.options;

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
public class DataValidateOptionsModel
    extends MVCModelBase
{

    @Getter private List<EditAccountModel> accountModels;
    @Getter private List<TickerModel> tickerModels;

    @Getter private ListDataProvider<ValidateOptionTransactionModel> gridDataProvider;
    @Getter private final List<ValidateOptionTransactionModel> dbList;

    @Getter @Setter private EditAccountModel selectedAccountModel;
    @Getter @Setter private TickerModel selectedTickerModel;
    @Getter @Setter private Boolean selectedSkip = false;
    @Getter @Setter private Boolean selectedValidated = false;

    /**
     * track when setting a filter. Do not want to enable save if only
     * a filter change on the data provider
     */
    @Getter @Setter private Boolean bInFilterChange = false;

    public DataValidateOptionsModel()
    {
        this.dbList = new ArrayList<>();
    }

    public void updateAccountModels()
    {
        this.accountModels = this.serviceTPC.getAccountModels();
    }

    public void updateTickerModels()
    {
        this.tickerModels = this.serviceTPC
            .getTickerModels(this.selectedAccountModel);
    }

    /**
     * retrieves data for the grid
     */
    public void updateGridData()
    {
        List<ValidateOptionTransactionModel> aList;
        ValidateOptionTransactionModel tmpVotm;

        this.dbList.clear();

        //refresh grid data
        aList = serviceTPC.getValidateOptionTransactionModels(
            this.selectedAccountModel, this.selectedTickerModel);
        for (ValidateOptionTransactionModel model : aList)
        {
            tmpVotm = ValidateOptionTransactionModel.builder()
                .joomlaId(model.getJoomlaId())
                .acctId(model.getAcctId())
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
            this.dbList.add(tmpVotm);
        }

        this.gridDataProvider = new ListDataProvider<>(aList);
        this.gridDataProvider.refreshAll();
    }

    /**
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
    }

    @Override
    public void getPrefs(String key)
    {
        this.prefsController.readPrefsByPrefix(key);
        this.selectedSkip = this.prefsController
            .getPref("OptionValidateSkip").equals("Yes");
        this.selectedValidated = this.prefsController
            .getPref("OptionValidateValidated").equals("Yes");
    }

    public void writePrefs(String skip, String validated)
    {
        //preferences, update the hashmap, then write to database
        this.prefsController.setPref("OptionValidateSkip", skip);
        this.prefsController.setPref("OptionValidateValidated", validated);
        this.prefsController.writePrefsByPrefix("OptionValidate");
    }

    void doSave()
    {
        Integer i;
        Iterator dataProviderIterator;
        ValidateOptionTransactionModel tmpModel;

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
            tmpModel = (ValidateOptionTransactionModel) dataProviderIterator.
                next();
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
                    tmpModel.getAcctId(),
                    tmpModel.getFiTId());
            }

            if (tmpModel.getBValidated().equals(this.dbList
                .get(i).getBValidated()))
            {
                //no change
            } else
            {
                //new validated
                //write to database
                this.writeTransaction(
                    tmpModel.getBSkip(),
                    tmpModel.getBValidated(),
                    tmpModel.getAcctId(),
                    tmpModel.getFiTId());
            }
            i++;
        }
    }

    private void writeTransaction(Boolean skip, Boolean validated,
        Integer acctId, String fiTId)
    {
        String sql;

        sql = String.format(ValidateOptionTransactionModel.SQL_UPDATE_INVTRAN,
            skip ? 1 : 0, validated ? 1 : 0, acctId, fiTId);

        this.serviceTPC.executeSQL(sql);
    }

    @Override
    public void writePrefs(String prefix)
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
