package com.hpi.tpc.services;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import java.text.*;
import java.util.*;
import java.util.Date;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.rowset.*;
import org.springframework.stereotype.*;

/**
 *
 */
@Repository
@Getter
public class TPCDAOImpl
{

    @Autowired private JdbcTemplate jdbcTemplate;

    public synchronized List<NoteModel> getByJId(String whereString)
    {
        String sql;
        List<NoteModel> notes;

        sql = "";
        switch (whereString)
        {
            case "mine":
                sql = String.format(NoteModel.SQL_GET_STRING
                    .concat(NoteModel.SQL_GET_WHERE_STRING),
                    SecurityUtils.getUserId(), NoteModel.ACTIVE_YES);
                break;
            case "all":
                sql = String.format(NoteModel.SQL_GET_STRING
                    .concat(NoteModel.SQL_GET_WHERE_ALL_STRING),
                    NoteModel.ACTIVE_YES);
                break;
            case "archived":
                sql = String.format(NoteModel.SQL_GET_STRING
                    .concat(NoteModel.SQL_GET_WHERE_STRING),
                    SecurityUtils.getUserId(), NoteModel.ACTIVE_NO);
                break;
        }

        notes = jdbcTemplate.query(sql, new NoteMapper());

        this.notesFix(notes);

        return notes;
    }

    private synchronized void notesFix(List<NoteModel> notes)
    {
        for (NoteModel nm : notes)
        {
            switch (nm.getAction())
            {
                case NoteModel.ACTION_BUY:
                    nm.setAction(NoteModel.ACTION_BUY_STRING);
                    break;
                case NoteModel.ACTION_SELL:
                    nm.setAction(NoteModel.ACTION_SELL_STRING);
                    break;
                case NoteModel.ACTION_WATCH:
                    nm.setAction(NoteModel.ACTION_WATCH_STRING);
                    break;
                case NoteModel.ACTION_HEDGE:
                    nm.setAction(NoteModel.ACTION_HEDGE_STRING);
                    break;
                case NoteModel.ACTION_OTHER:
                    nm.setAction(NoteModel.ACTION_OTHER_STRING);
                    break;
                case NoteModel.ACTION_HOLD:
                    nm.setAction(NoteModel.ACTION_HOLD_STRING);
                    break;
                default:
            }

            switch (nm.getTriggerType())
            {
                case NoteModel.ALERT_DATE:
                    nm.setTriggerType(NoteModel.ALERT_DATE_STRING);
                    break;
                case NoteModel.ALERT_PRICE:
                    nm.setTriggerType(NoteModel.ALERT_PRICE_STRING);
                    break;
                case NoteModel.ALERT_OTHER:
                    nm.setTriggerType(NoteModel.ALERT_OTHER_STRING);
                    break;
                default:
            }
        }
    }

    public synchronized List<ActionModel> getActionModels(String sql)
    {
        List<ActionModel> actionModels;

        actionModels = jdbcTemplate.query(sql, new ActionMapper());

        return actionModels;
    }

    public synchronized List<AlertTypeModel> getAlertTypeModels(String sql)
    {
        List<AlertTypeModel> alertTypeModels;

        alertTypeModels = jdbcTemplate.query(sql, new AlertTypeMapper());

        return alertTypeModels;
    }

    public synchronized List<SupportResistanceModel>
        getSupportResistanceModels(String sql)
    {
        List<SupportResistanceModel> supportResistanceModels;

        supportResistanceModels
            = jdbcTemplate.query(sql, new SupportResistanceMapper());

        return supportResistanceModels;
    }

    /**
     * Given a noteModel, add it to the database
     *
     * @param noteModel
     */
    public synchronized void saveNote(NoteModel noteModel)
    {
        String sql;
        Double newPrice;
        Double newUnits;
        String newDateTime;
        Date aDateTime;
        SimpleDateFormat sdf;

        //defaults
        newPrice = 0.0;
        newUnits = 0.0;

        newPrice = noteModel.getIPrice();

        newUnits = noteModel.getUnits();

        //adjust timestamp to match the database
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //needs to be in timezone of the database server
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        aDateTime = new Date();
        newDateTime = sdf.format(aDateTime);

        sql = NoteModel.SQL_INSERT_STRING_1;
        sql += noteModel.getJoomlaId() + "', '";
        sql += noteModel.getTicker().toUpperCase() + "', '";
        sql += noteModel.getDescription().replace("'", "\\'") + "', '";
        sql += noteModel.getNotes().replace("'", "\\'") + "', '";
        sql += newUnits + "', '";
        sql += noteModel.getAction().isEmpty() ? ActionModel.ACTIONS_BUY : noteModel.getAction();
        sql += "', '";
        sql += noteModel.getTriggerType().isEmpty() ? AlertTypeModel.ALERTS_PRICE : noteModel.getTriggerType();
        sql += "', '";
        sql += noteModel.getTrigger().replace("'", "\\'") + "', '";
        sql += newPrice;
        sql += "', '";
        sql += noteModel.getActive() + "', '";
//        sql += noteModel.getDateEntered();
        sql += newDateTime;
        sql += NoteModel.SQL_INSERT_STRING_2;

        jdbcTemplate.execute(sql);
    }

    public synchronized void executeSQL(String sql)
    {
        jdbcTemplate.execute(sql);
    }

    /**
     * Given a noteModel, update it in the database.
     * Only the following may be edited when editing an existing note:
     * <ul>
     * <li>Action</li>
     * <li>Notes</li>
     * <li>Quantity</li>
     * <li>Alert Type</li>
     * <li>Alert</li>
     * </ul>
     *
     * @param noteModel
     */
    public synchronized void updateNote(NoteModel noteModel)
    {
        String sql;
        String newDateTime;
        Double newPrice;
        Double newUnits;
        Date aDateTime;
        SimpleDateFormat sdf;

        newPrice = 0.0;
        newUnits = 0.0;

        newPrice = noteModel.getIPrice();

        newUnits = noteModel.getUnits();

        //adjust timestamp to match the database
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //needs to be in timezone of the database server
        //not sure that is correct
//        sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
//        TimeZone.setDefault(TimeZone.getTimeZone("America/Chicago"));
        aDateTime = new Date(noteModel.getTStamp());
        newDateTime = sdf.format(aDateTime);
        sql = NoteModel.SQL_UPDATE_STRING;

        sql = String.format(sql, //noteModel.getTStamp(),
            noteModel.getDescription().replace("'", "\\'"),
            noteModel.getNotes().replace("'", "\\'"), newUnits,
            noteModel.getAction(), noteModel.getTriggerType(),
            noteModel.getTrigger().replace("'", "\\'"), newPrice, noteModel.getActive(),
            noteModel.getJoomlaId(), noteModel.getTicker(), newDateTime);

        jdbcTemplate.execute(sql);
    }

    public synchronized void AppTracking(String value)
    {
        String sql;

        if (SecurityUtils.getUserId() == 816 || SecurityUtils.getUserId() == null)
        {
            return;
        }

        sql
            = "insert into hlhtxc5_dmOfx.AppTracking (JoomlaId, Action) values ('"
            + SecurityUtils.getUserId() + "', '" + value + "');";

        jdbcTemplate.execute(sql);
    }

    public List<String> getOptionExpiry(String underlying,
        String minStrike, String maxStrike)
    {
        String sql;
        SqlRowSet rowSet;
        List<String> expiry;

        expiry = new ArrayList<>();

        sql
            = "select distinct ExpirationDate from hlhtxc5_dmOfx.OptionHistory where Symbol = '%s' and ExpirationDate >= now() and StrikePrice >= '%s' and StrikePrice <= '%s' order by ExpirationDate";

        sql = String.format(sql, underlying, minStrike, maxStrike);

        rowSet = jdbcTemplate.queryForRowSet(sql);

        while (rowSet.next())
        {
            expiry.add(rowSet.getString("ExpirationDate"));
        }
        return expiry;
    }

//    public List<OHLCVModel> getEquityOHLCVData(String ticker)
//    {
//        String sql;
//        SqlRowSet rowSet;
//        List<OHLCVModel> ohlcvms;
//
//        sql = String.format(OHLCVModel.getSQL_DAILY(), ticker);
//
//        ohlcvms = jdbcTemplate.query(sql, new OHLCVMapper());
//
//        return ohlcvms;
//    }
    public synchronized List<ClientEquityModel> getClientEquityAttributesModels()
    {
        List<ClientEquityModel> clientEquityAttributesModels;
        String sql;

        sql = String.format(
            ClientEquityModel.SQL_SELECT_ALL_EQUITIES_JOOMLAID_ORDER_TICKER,
            SecurityUtils.getUserId().toString());

        clientEquityAttributesModels = jdbcTemplate.query(sql,
            new ClientEquityMapper());

        return clientEquityAttributesModels;
    }

//    public synchronized List<TimeseriesModel> getTimeseriesModels(String sql)
//    {
//        List<TimeseriesModel> timeseriesModels;
//
//        timeseriesModels = jdbcTemplate.query(sql, new TimeseriesMapper());
//
//        return timeseriesModels;
//    }
//    public synchronized List<GainModel> getGainsModels(String sql)
//    {
//        List<GainModel> gainsModels;
//
//        gainsModels = jdbcTemplate.query(sql, new GainMapper());
//
//        return gainsModels;
//    }
    public synchronized List<ValidateStockTransactionModel>
        getValidateStockTransactionModels(
            EditAccountModel accountModel, TickerModel tickerModel)
    {
        List<ValidateStockTransactionModel> validateStockTransactionModels;

        validateStockTransactionModels = jdbcTemplate.query(
            String.format(ValidateStockTransactionModel.SQL_STRING,
                accountModel.getDbAcctId(),
                tickerModel.getTicker(),
                SecurityUtils.getUserId(),
                accountModel.getDbAcctId(),
                tickerModel.getTicker(),
                SecurityUtils.getUserId(),
                accountModel.getDbAcctId(),
                tickerModel.getTicker(),
                SecurityUtils.getUserId(),
                accountModel.getDbAcctId(),
                tickerModel.getTicker(),
                SecurityUtils.getUserId(),
                accountModel.getDbAcctId(),
                tickerModel.getTicker(),
                SecurityUtils.getUserId()),
            new ValidateStockTransactionMapper());

        return validateStockTransactionModels;
    }

    public synchronized List<EditAccountModel> getAccountModels()
    {
        return jdbcTemplate.query(String.format(
            EditAccountModel.SQL_ALL_ACCOUNTS, SecurityUtils.getUserId()), new EditAccountMapper());
    }

    public synchronized List<OfxInstitutionModel> getOfxInstitutionModels()
    {
        return jdbcTemplate.query(OfxInstitutionModel.SQL_ALL_INSTITUTIONS, new OfxInstitutionMapper());
    }

    public synchronized List<OfxInstitutionModel> getClientOfxInstitutionModels()
    {
        return jdbcTemplate.query(
            String.format(OfxInstitutionModel.SQL_CLIENT_INSTITUTIONS,
                SecurityUtils.getUserId()),
            new OfxInstitutionMapper());
    }

    public synchronized List<ValidateOptionTransactionModel>
        getValidateOptionTransactionModels(EditAccountModel accountModel, TickerModel tickerModel)
    {
        List<ValidateOptionTransactionModel> validateOptionTransactionModels;
        String sql;

        sql = String.format(ValidateOptionTransactionModel.SQL_STRING,
            accountModel.getDbAcctId(),
            tickerModel.getTicker(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            tickerModel.getTicker(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            tickerModel.getTicker(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            tickerModel.getTicker(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            tickerModel.getTicker(),
            SecurityUtils.getUserId());

        validateOptionTransactionModels = jdbcTemplate.query(
            sql, new ValidateOptionTransactionMapper());

        return validateOptionTransactionModels;
    }

    public synchronized List<TickerModel> getTickerModels(
        EditAccountModel accountModel)
    {
        return jdbcTemplate.query(String.format(
            ValidateOptionTransactionModel.SQL_TICKERS,
            accountModel.getDbAcctId(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            SecurityUtils.getUserId(),
            accountModel.getDbAcctId(),
            SecurityUtils.getUserId()),
            new TickerMapper());
    }

    public synchronized List<ClientSectorModel> getClientSectorModels(String sql)
    {
        return jdbcTemplate.query(String.format(sql, SecurityUtils.getUserId()
            .toString()), new ClientSectorMapper());
    }

    public synchronized List<FIFOOpenTransactionModel> getFIFOOpenPositionModels(String sql)
    {
        return jdbcTemplate.query(sql, new FIFOOpenTransactionMapper());
    }

    public synchronized List<FIFOClosedTransactionModel> getFIFOClosedPositionModels(String sql)
    {
        return jdbcTemplate.query(sql, new FIFOClosedTransactionMapper());
    }

    public synchronized Integer getClientSectorCount(String sql)
    {
        SqlRowSet rs;
        Integer rows;

        rows = 0;
        rs = jdbcTemplate.queryForRowSet(sql);

        while (rs.next())
        {
            //only one row
            rows = rs.getInt("count(*)");
        }

        return rows;
    }

    public synchronized Integer getClientSectorIdFromTicker(String tkr)
    {
        SqlRowSet rs;
        Integer clientSectorId;

        clientSectorId = 0;
        rs = jdbcTemplate.queryForRowSet(String.format(
            ClientSectorModel.SQL_SECTORID_FROM_TKR,
            tkr, SecurityUtils.getUserId()));

        while (rs.next())
        {
            //only one row
            clientSectorId = rs.getInt("ClientSectorId");
        }

        return clientSectorId;
    }
}
