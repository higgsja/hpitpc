package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

/**
 *
 * Data model for finViz.com data extract
 */
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode
@Builder
public class FinVizEquityInfoModel {
    public static List<FinVizEquityInfoModel> FIN_VIZ_MODELS;
    public static final String SQL_GET_LATEST_DATE;
    public static final String SQL_GET_LATEST_FILTERED;
    public static final String SQL_GET_LATEST_FILTERED_COUNT;
    //leave obsolete has hashtable is synchronized

    public static final Hashtable<String, FinVizEquityInfoModel> FIN_VIZ_HASH_TABLE;

    public synchronized static List<FinVizEquityInfoModel> getFIN_VIZ_MODELS() {
        return FIN_VIZ_MODELS;
    }

//    public synchronized static String getSQL_GET_STRING() {
//        return SQL_GET_STRING;
//    }

    static {
        SQL_GET_LATEST_DATE =
            "select * from hlhtxc5_dmOfx.EquityInfo where `Date` = (select MAX(`Date`) as `Date` from hlhtxc5_dmOfx.EquityInfo limit 1) order by Ticker asc;";
        
        SQL_GET_LATEST_FILTERED = "select * from hlhtxc5_dmOfx.EquityInfo as ei where `Date` = (select MAX(`Date`) as `Date` from hlhtxc5_dmOfx.EquityInfo limit 1) and if('%s' = '', ei.Ticker like '%%', ei.Ticker like '%s%%') and if('%s' = '', ei.Sector like '%%', ei.Sector like '%s%%') and if ('%s' = '', ei.Industry like '%%', ei.Industry like '%s%%') order by Ticker asc limit %s, %s;";
        
        SQL_GET_LATEST_FILTERED_COUNT = "select count(*) from hlhtxc5_dmOfx.EquityInfo as ei where `Date` = (select MAX(`Date`) as `Date` from hlhtxc5_dmOfx.EquityInfo limit 1) and if('%s' = '', ei.Ticker like '%%', ei.Ticker like '%s%%') and if('%s' = '', ei.Sector like '%%', ei.Sector like '%s%%') and if ('%s' = '', ei.Industry like '%%', ei.Industry like '%s%%') ;";

        FIN_VIZ_MODELS = new ArrayList<>();
        FIN_VIZ_HASH_TABLE = new Hashtable<>();
    }

    String Ticker;  //primary
    String Company;
    String Sector;
    String Industry;
    String Country;
    String MktCap;
    String PE;
    String FwdPE;
    String PEG;
    String Div;
    String PayoutRatio;
    String EPS;
    String EPSCY;
    String EPSNY;
    String EPSP5Y;
    String EPSN5Y;
    String ATR;
    String SMA20;
    String SMA50;
    String SMA200;
    String Hi50d;
    String Lo50d;
    String Hi52w;
    String Lo52w;
    String RSI;
    String AnRec;
    String Price;
    String Volume;
    String EarnDate;
    String TgtPrice;
    String date;
    String Beta;
}
