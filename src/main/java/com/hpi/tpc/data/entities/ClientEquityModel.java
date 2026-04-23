package com.hpi.tpc.data.entities;

import com.hpi.tpc.app.security.*;
import lombok.*;

@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class ClientEquityModel
    implements Comparable<ClientEquityModel> {
    @Getter @Setter private Integer joomlaId;
    @Getter @Setter private String ticker;
    @Getter @Setter private String tickerIEX;
    @Getter @Setter private String company;
    @Getter private String active;
    @Getter private Boolean bActive;
    @Getter @Setter private Integer clientSectorId;
    @Getter @Setter private String cSecShort;
//    @Getter @Setter private ClientSectorModel clientSectorModel;
    @Getter @Setter private Double tgtPct;
    @Getter @Setter private Double adjPct;
    @Getter @Setter private Double analystTgt;
    @Getter @Setter private Double stkPrice;
    @Getter @Setter private String comment;
    @Getter private String tgtLocked;
    @Getter private Boolean bTgtLocked;
    @Getter @Setter private Double actPct;
    @Getter @Setter private Double price;
    @Getter @Setter private Integer changed;
    
    public static final String SQL_SELECT_ALL_EQUITIES_JOOMLAID_ORDER_TICKER;
    public static final String SQLINSERT;
    public static final String SQL_UPDATE_ALLOCATION;
    public static final String SQL_UPDATE;
    
    
    
    public static final int CHANGE_NONE = 0;
    public static final int CHANGE_EDIT = 1;
    public static final int CHANGE_NEW = 2;

    static {
        SQL_SELECT_ALL_EQUITIES_JOOMLAID_ORDER_TICKER = "select cea.Ticker, ei.Company, csl.ClientSectorId, CSecShort, cea.Active, cea.Comment, cea.JoomlaId, TickerIEX, cea.TgtPct as TgtPct, cea.AnalystTgt, cea.StkPrice, cea.TgtLocked, cea.ActPct from hlhtxc5_dmOfx.ClientEquityAttributes as cea, hlhtxc5_dmOfx.ClientSectorList as csl, (select distinct Ticker, Company from hlhtxc5_dmOfx.EquityInfo where `Date` > date_sub(now(), interval 2 day)) as ei where cea.ClientSectorId = csl.ClientSectorId and cea.JoomlaId = csl.JoomlaId and cea.JoomlaId = '%s' and ei.Ticker = cea.Ticker order by Ticker asc";
            
        SQLINSERT =
            "insert into hlhtxc5_dmOfx.ClientEquityAttributes (JoomlaId, Ticker, TickerIEX, Active, ClientSectorId, TgtPct, AnalystTgt, StkPrice, Comment, TgtLocked, ActPct) values (";

        SQL_UPDATE_ALLOCATION =
            "update hlhtxc5_dmOfx.ClientEquityAttributes set Active = '%s', TgtPct = '%s', Comment = '%s', TgtLocked = '%s' where JoomlaId = '%s' and Ticker = '%s'";

        SQL_UPDATE = "update hlhtxc5_dmOfx.ClientEquityAttributes set ";
    }

    public ClientEquityModel() {
        //default clientEquityModel
        this.joomlaId = SecurityUtils.getUserId();
        this.ticker = null;
        this.tickerIEX = null;
        this.active = "Yes";
        this.bActive = true;
        this.clientSectorId = null;
        this.cSecShort = null;
//        this.clientSectorModel = null;
        this.tgtPct = 0.0;
        this.adjPct = 0.0;
        this.analystTgt = 0.0;
        this.stkPrice = 0.0;
        this.comment = "";
        this.tgtLocked = "No";
        this.bTgtLocked = false;
        this.actPct = 0.0;
        this.price = 0.0;
        this.changed = ClientEquityModel.CHANGE_NEW;
    }

    public ClientEquityModel(ClientEquityModel cem) {
        //default model
        this(cem.joomlaId, cem.ticker, cem.tickerIEX, cem.company, cem.active, cem.bActive, cem.clientSectorId,
            cem.getCSecShort(), 
            cem.tgtPct, cem.adjPct, cem.analystTgt, cem.stkPrice,
            cem.comment, cem.tgtLocked, cem.bTgtLocked, cem.actPct, cem.price, cem.changed);
    }

    @Override
    public int compareTo(ClientEquityModel csm) {
        return this.getTicker().compareTo(csm.getTicker());
    }

    @Override
    public String toString() {
        return this.ticker;
    }

    public void setBActive(Boolean bActive) {
        this.bActive = bActive;

        this.active = this.bActive ? "Yes" : "No";
    }

    public void setActive(String active) {
        this.active = active;

        this.bActive = this.active.equalsIgnoreCase("yes");
    }

    public void setBTgtLocked(Boolean bTgtLocked) {

        this.bTgtLocked = bTgtLocked;

        this.tgtLocked = this.bTgtLocked ? "Yes" : "No";
    }

    public void setTgtLocked(String tgtLocked) {
        this.tgtLocked = tgtLocked;

        this.bTgtLocked = this.tgtLocked.equalsIgnoreCase("yes");
    }
}
