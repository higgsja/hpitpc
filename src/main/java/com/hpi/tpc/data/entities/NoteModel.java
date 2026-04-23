package com.hpi.tpc.data.entities;

import java.text.*;
import java.time.format.*;
import java.util.*;
import lombok.*;

@NoArgsConstructor
@Getter @Setter
@EqualsAndHashCode @ToString
@Builder
public class NoteModel
{

    private final ArrayList<NoteModel> noteModels = new ArrayList<>();

    public static final String SQL_GET_STRING;
    public static final String SQL_GET_WHERE_STRING;
    public static final String SQL_GET_WHERE_ALL_STRING;
//    public static final String SQL_GET_TKR_STRING;
    public static final String SQL_UPDATE_STRING;
    public static final String SQL_INSERT_STRING_1;
    public static final String SQL_INSERT_STRING_2;
    public static final String SQL_UPDATE_STRING_2;

    public static final DateTimeFormatter DATETIME_FORMATTER;
    public static final DateTimeFormatter DATETIME_FORMATTER_EARNINGS;
    public static final DateTimeFormatter DATETIME_FORMATTER_TSTAMP;
    public static final DateFormat DATE_FORMAT;

    public static final String ACTION_BUY_STRING = "Buy";
    public static final String ACTION_SELL_STRING = "Sell";
    public static final String ACTION_WATCH_STRING = "Watch";
    public static final String ACTION_HEDGE_STRING = "Hedge";
    public static final String ACTION_OTHER_STRING = "Other";
    public static final String ACTION_HOLD_STRING = "Hold";
    public static final String ACTION_BUY = "1";
    public static final String ACTION_SELL = "2";
    public static final String ACTION_WATCH = "3";
    public static final String ACTION_HEDGE = "4";
    public static final String ACTION_OTHER = "5";
    public static final String ACTION_HOLD = "6";

    public static final Integer ACTION_BUY_INT = 1;
    public static final Integer ACTION_SELL_INT = 2;
    public static final Integer ACTION_WATCH_INT = 3;
    public static final Integer ACTION_HEDGE_INT = 4;
    public static final Integer ACTION_OTHER_INT = 5;
    public static final Integer ACTION_HOLD_INT = 6;

    public static final String ALERT_DATE_STRING = "Date";
    public static final String ALERT_PRICE_STRING = "Price";
    public static final String ALERT_OTHER_STRING = "Other";
    public static final String ALERT_DATE = "1";
    public static final String ALERT_PRICE = "2";
    public static final String ALERT_OTHER = "3";
    public static final Integer ALERT_DATE_INT = 1;
    public static final Integer ALERT_PRICE_INT = 2;
    public static final Integer ALERT_OTHER_INT = 3;


    public static final String ACTIVE_NO_STRING = "No";
    public static final String ACTIVE_YES_STRING = "Yes";
    public static final String ACTIVE_NO = "0";
    public static final String ACTIVE_YES = "1";

    @NonNull private String joomlaId;
    @NonNull private Long tStamp;
    @NonNull private String ticker;
//    @NonNull private String iPrice;
    @NonNull private Double iPrice;
    @NonNull private String description;
    @Builder.Default private String notes = "";
    @Builder.Default private Double units = 0.0;
    @NonNull private String action;
    @NonNull private String triggerType;
    @NonNull private String trigger;
    @NonNull private String active;
    @NonNull private String dateEntered;
    private Double high;
    private Double low;
    private Double close;
    private Double priceChange;
    private String priceChangePct;
    private Double gain;
    private String gainPct;
    private Double atr;
    private String earnDate;

    static
    {

        SQL_GET_STRING = "select nd.JoomlaId, nd.TStamp, nd.Ticker, nd.Description, nd.Notes, nd.Action, nd.TriggerType, nd.`Trigger`, nd.Active, ei.Company, ei.`MktCap(B)`, ei.50dHi, ei.50dLo, replace(lds.`Open`, ',', '') as Open, replace(lds.High, ',', '') as High, replace(lds.Low, ',', '') as Low, replace(lds.`Close`, ',', '') as Close, if(lds.Volume = null, null, lds.Volume / 1000000) as Volume, nd.`Units`, ei.52wHi, ei.52wLo, replace(if (nd.IPrice = null, lds.`Close`, nd.IPrice), ',', '') as IPrice, if (nd.IPrice = null, 0, lds.`Close` - nd.IPrice) as PriceChange, replace(format(if (nd.IPrice = null, 0, if (IPrice = 0, 0, 100 * (lds.`Close` - nd.IPrice) / nd.IPrice)),2), ',', '') as PriceChangePct, if (nd.`Units` = null, 0, if (nd.IPrice = null, 0, if (nd.Action = '2', replace(-nd.Units * (lds.`Close` - nd.IPrice), ',', ''), replace(nd.Units * (lds.`Close` - nd.IPrice), ',', '')))) as Gain, replace(format(if (nd.`Units` = null, 0, if (nd.IPrice = null, 0, if(nd.Action='2', -100 * (lds.`Close` - nd.IPrice) / nd.IPrice, 100 * (lds.`Close` - nd.IPrice) / nd.IPrice))),2), ',', '') as GainPct, ei.Sector, ei.Industry, 0 as Beta, replace(ei.PE, ',', '') as PE, replace(ei.FwdPE, ',', '') as FwdPE, ei.PEG, ei.Div, ei.ATR, ei.SMA200, ei.SMA50, ei.SMA20, ei.RSI, ei.AnRec, replace(ei.TgtPrice, ',', '') as TgtPrice, ei.EarnDate, replace(lds.open, ',', '') as open, nd.Active, nd.DateEntered from hlhtxc5_dmOfx.NotesData as nd, hlhtxc5_dmOfx.Util_LastDailyStock as lds, (select * from hlhtxc5_dmOfx.EquityInfo where `Date` = (select max(`Date`) from hlhtxc5_dmOfx.EquityInfo)) as ei where nd.Ticker = lds.EquityId and nd.Ticker = ei.Ticker";
//            "select nd.JoomlaId, nd.TStamp, nd.Ticker, nd.Description, nd.Notes, nd.Action, nd.TriggerType, nd.`Trigger`, nd.Active, ei.Company, ei.`MktCap(B)`, ei.50dHi, ei.50dLo, lds.`Open`, lds.High, lds.Low, lds.`Close`, if(lds.Volume = null, null, lds.Volume / 1000000) as Volume, nd.`Units`, ei.52wHi, ei.52wLo, if (nd.IPrice = null, lds.`Close`, nd.IPrice) as IPrice, if (nd.IPrice = null, 0, lds.`Close` - nd.IPrice) as PriceChange, format(if (nd.IPrice = null, 0, if (IPrice = 0, 0, 100 * (lds.`Close` - nd.IPrice) / nd.IPrice)),2) as PriceChangePct, if (nd.`Units` = null, 0, if (nd.IPrice = null, 0, if (nd.Action = '2', -nd.Units * (lds.`Close` - nd.IPrice), nd.Units * (lds.`Close` - nd.IPrice)))) as Gain, format(if (nd.`Units` = null, 0, if (nd.IPrice = null, 0, if(nd.Action='2', -100 * (lds.`Close` - nd.IPrice) / nd.IPrice, 100 * (lds.`Close` - nd.IPrice) / nd.IPrice))),2) as GainPct, ei.Sector, ei.Industry, 0 as Beta, ei.PE, ei.FwdPE, ei.PEG, ei.Div, ei.ATR, ei.SMA200, ei.SMA50, ei.SMA20, ei.RSI, ei.AnRec, ei.TgtPrice, ei.EarnDate, lds.Open,  nd.Active, nd.DateEntered from hlhtxc5_dmOfx.NotesData as nd, hlhtxc5_dmOfx.Util_LastDailyStock as lds, (select * from hlhtxc5_dmOfx.EquityInfo where `Date` = (select max(`Date`) from hlhtxc5_dmOfx.EquityInfo)) as ei where nd.Ticker = lds.EquityId and nd.Ticker = ei.Ticker "; 

        SQL_GET_WHERE_STRING = " and nd.JoomlaId = '%s' and nd.Active = '%s' order by nd.TStamp desc;";

        SQL_GET_WHERE_ALL_STRING = " and nd.Active = '%s' order by nd.TStamp desc";

        SQL_INSERT_STRING_1
            = "insert into hlhtxc5_dmOfx.NotesData (JoomlaId, Ticker, Description, Notes, Units, Action, TriggerType, `Trigger`, IPrice, Active, DateEntered) values ('";

        SQL_INSERT_STRING_2 = "');";

        SQL_UPDATE_STRING_2 = "');";

        SQL_UPDATE_STRING
            = "update hlhtxc5_dmOfx.NotesData set Description = '%s', Notes = '%s', Units = '%s', Action = '%s', TriggerType = '%s', `Trigger` = '%s', IPrice = '%s', Active = '%s' where JoomlaId = '%s' and Ticker = '%s' and TStamp = '%s';";

        DATETIME_FORMATTER_EARNINGS
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DATETIME_FORMATTER_TSTAMP
            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    }

    public NoteModel(String joomlaId, Long tStamp, String ticker,
        Double iPrice, String description, String notes, Double units,
        String action, String triggerType, String trigger, String active,
        String dateEntered, Double high, Double low, Double close,
        Double priceChange, String pctPriceChange,
        Double gain, String gainPct, Double atr, String earnDate)
    {
        this.joomlaId = joomlaId;
        this.tStamp = tStamp;
        this.ticker = ticker;
        this.iPrice = iPrice;
        this.description = description;
        this.notes = notes;
        this.units = units;
        this.action = action;
        this.triggerType = triggerType;
        this.trigger = trigger;
        this.active = active;
        this.dateEntered = dateEntered;
        this.high = high;
        this.low = low;
        this.close = close;
        this.priceChange = priceChange;
        this.priceChangePct = pctPriceChange;
        this.gain = gain;
        this.gainPct = gainPct;
        this.atr = atr;
        this.earnDate = earnDate;
    }

    public NoteModel(String joomlaId, Long tStamp, String ticker,
        Double iPrice, String description, String notes, Double units,
        String action, String triggerType, String trigger, String active,
        String dateEntered)
    {
        this.joomlaId = joomlaId;
        this.tStamp = tStamp;
        this.ticker = ticker;
        this.iPrice = iPrice;
        this.description = description;
        this.notes = notes;
        this.units = units;
        this.action = action;
        this.triggerType = triggerType;
        this.trigger = trigger;
        this.active = active;
        this.dateEntered = dateEntered;
    }

    public static Integer setActionInt(String action)
    {
        switch (action)
        {
            case "Buy":
                return ACTION_BUY_INT;
            case "Sell":
                return ACTION_SELL_INT;
            case "Watch":
                return ACTION_WATCH_INT;
            case "Hedge":
                return ACTION_HEDGE_INT;
            case "Other":
                return ACTION_OTHER_INT;
            case "Hold":
                return ACTION_HOLD_INT;
        }
        return 0;
    }

    public static Integer setTriggerInt(String trigger)
    {
        switch(trigger){
            case "Date": return ALERT_DATE_INT;
                case "Price": return ALERT_PRICE_INT;
            case "Other": return ALERT_OTHER_INT;
        }
        return 0;
    }
}
