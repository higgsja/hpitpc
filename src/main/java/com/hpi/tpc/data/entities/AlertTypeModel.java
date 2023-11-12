package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

@Getter
@Builder
public class AlertTypeModel {

    //global constants
    public static final Integer ALERTS_DATE = 1;
    public static final Integer ALERTS_PRICE = 2;
    public static final Integer ALERTS_OTHER = 3;

    public static List<AlertTypeModel> ALERT_TYPE_LIST;
    public static final String SQL_STRING;

    //local
    private final Integer kVal;
    private final String triggerType;
    private final Integer active;

    static {
        ALERT_TYPE_LIST = new ArrayList<>();
        SQL_STRING =
            "select * from hlhtxc5_dmOfx.NotesTriggerTypeList order by TriggerType";
    }

    @Override
    public String toString() {
        return this.triggerType;
    }

//    public String getTriggerType()
//    {
//        return this.triggerType;
//    }
//    
//    public Integer getActive()
//    {
//        return this.active;
//    }
//    
//    public Integer getKVal()
//    {
//        return this.kVal;
//    }
}
