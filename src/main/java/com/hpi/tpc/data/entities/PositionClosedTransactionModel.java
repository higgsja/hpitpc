package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
/*
 * Holds a transaction as one leg of a position
 */
public class PositionClosedTransactionModel {

    private Integer dmAcctId;
    private Integer joomlaId;
    private Integer positionId;
    private String fiTId;
    private String equityId;
    private String transactionName;
    private String ticker;
    private java.sql.Date dateOpen;
    private java.sql.Date dateClose;  //use the latest date in cases where multiple close dates
    private Double units;
    private Double priceOpen;
    private Double priceClose;
    private java.sql.Date dateExpire;
    private Integer days;
    private String positionType;
    private Double totalOpen;
    private Double totalClose;
    private String equityType;
    private Double gain;
    private Double gainPct;
    private String transactionType;
    private Integer complete;
    private Boolean bComplete;

    //one or more transactions make up the transaction model (lots)
    @Builder.Default @Getter private final 
        ArrayList<FIFOClosedTransactionModel> fifoClosedTransactionModels = new ArrayList<>();
    
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
