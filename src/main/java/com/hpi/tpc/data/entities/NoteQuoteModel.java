package com.hpi.tpc.data.entities;

/*
 * last: Jul 2020
 */
import lombok.*;

@AllArgsConstructor
@Getter @Setter
@Builder
/**
 * Used to retrieve quotes for notes
 */
public class NoteQuoteModel {

    private String ticker;
    private String company;
    private Double close;
    
    @Override
    public String toString() {
        return this.ticker;
    }
}
