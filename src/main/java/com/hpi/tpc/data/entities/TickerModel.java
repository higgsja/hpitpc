package com.hpi.tpc.data.entities;

import lombok.*;

@AllArgsConstructor
@Getter
@Builder
public class TickerModel {

    private final String ticker;

    static {

    }

    @Override
    public String toString() {
        return this.ticker;
    }
}
