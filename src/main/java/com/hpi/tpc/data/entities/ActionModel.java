package com.hpi.tpc.data.entities;

import java.util.*;
import lombok.*;

@Getter
@Builder
public class ActionModel {

    public static List<ActionModel> ACTION_LIST;
    public static final String SQL_STRING;

    public static final Integer ACTIONS_BUY = 1;
    public static final Integer ACTIONS_SELL = 2;
    public static final Integer ACTIONS_WATCH = 3;
    public static final Integer ACTIONS_HEDGE = 4;
    public static final Integer ACTIONS_OTHER = 5;
    public static final Integer ACTIONS_HOLD = 6;

    private Integer kVal;
    private String action;
    private Integer active;

    static {
        ACTION_LIST = new ArrayList<>();
        SQL_STRING =
            "select * from hlhtxc5_dmOfx.NotesActionList order by Action";
    }

    @Override
    public String toString() {
        return this.action;
    }
}
