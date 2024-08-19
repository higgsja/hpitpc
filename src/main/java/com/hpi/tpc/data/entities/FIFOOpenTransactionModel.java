package com.hpi.tpc.data.entities;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class FIFOOpenTransactionModel
{
//    public static final String FROM_FITID_LIST1
//        = "select * from hlhtxc5_dmOfx.FIFOOpenTransactions where JoomlaId = '%s' and FiTId in (";

//    public static final String FROM_FITID_LIST2 = ") order by GMTDtTrade desc;";

//    public static final String GET_ALL = "";
    
    

    private Integer dmAcctId;
    private Integer joomlaId;
    private String fiTId;
    private String ticker;
    private String equityId;
    private String transactionName;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;
    private java.sql.Date dateExpire;
    private Double units;
    private Double priceOpen;
    private Double priceClose;
    private Double totalOpen;
    private Double totalClose;
    private Double gain;
    private Double gainPct;
    private String equityType;
    private String positionType;
    private String transactionType;
    private Integer complete;
    private String optionType;
    private Double strikePrice;
    private Integer shPerCtrct;
    private Integer days;
    private Integer clientSectorId;
    private Double mktVal;
    private Double lMktVal;
    private Double actPct;
    private Boolean bComplete;
}
