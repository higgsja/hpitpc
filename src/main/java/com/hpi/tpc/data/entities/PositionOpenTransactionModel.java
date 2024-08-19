package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
/*
 * Holds a transaction as one leg of a position
 */
public class PositionOpenTransactionModel {
    
    public static final String GET_OPTION_BY_EQUITYID_DATEOPEN_JOOMLAID = "";
    
    private Integer dmAcctId;
    private Integer joomlaId;
    private Integer positionId;
    private String fiTId;
//    private String equityId;  cannot have for options do drop for all
    private String transactionName;
    private String ticker;
    private java.sql.Date dateOpen;
    private Double units;
    private Double priceOpen;
    private java.sql.Date dateExpire;
    private Integer days;
    private String positionType;
    private Double totalOpen;
    private String equityType;
    private Double gain;
    private Double gainPct;
    private String transactionType;
    private Integer complete;
    private Boolean bComplete;
    private Double mktVal;
    private Double lMktVal;
//    private Double actPct;    not used for positions
    
    //one or more transactions make up the transaction model (lots)
    @Builder.Default @Getter private final 
        ArrayList<FIFOOpenTransactionModel> fifoOpenTransactionModels = new ArrayList<>();

    public void setBComplete(Boolean complete) {
        this.bComplete = complete;

        if (this.bComplete) {
            this.complete = 1;
        }
        else {
            this.complete = 0;
        }
    }

    public void setComplete(Integer complete) {
        this.complete = complete;

        this.bComplete = this.complete == 1;
    }

}
