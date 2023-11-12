package com.hpi.tpc.ui.views.menus.data;

import lombok.*;

@Getter @Setter
@EqualsAndHashCode
public class Attribute
{
    private final String attribute;
    
    public Attribute(final String attribute){
        this.attribute = attribute;
    }
    
    @Override
    public String toString(){
        return this.getAttribute();
    }
}
