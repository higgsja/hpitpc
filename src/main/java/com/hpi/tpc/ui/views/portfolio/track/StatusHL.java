package com.hpi.tpc.ui.views.portfolio.track;

import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.component.dependency.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.spring.annotation.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Component
@NoArgsConstructor
@CssImport("./styles/portfolioTracking.css")
public class StatusHL
    extends ControlsHLBase
{

    @Autowired public TrackingMVC_M trackingMVCModel;
    @Autowired public JdbcTemplate jdbcTemplate;

    private final HorizontalLayout statusHL = new HorizontalLayout();
    @Getter private final NativeLabel labelStatus = new NativeLabel();

    @PostConstruct
    public void construct_a()
    {
        //docs indicate abstract postConstruct will not be hit but it is
        this.setClassName("trackingControlsStatusHL");
        
        this.setHeight("29px");
    }

    @Override
    public void doLayout()
    {
        this.setupStatus();
    }

    private void setupStatus()
    {
        this.statusHL.setClassName("trackingControlsStatusLabel");
//        this.setWidth("360px");
//        this.setHeight("29px");

        //vertical alignment
//        this.setAlignItems(Alignment.CENTER);
        this.statusHL.add(this.labelStatus);
        this.add(this.statusHL);
    }

    public void setStatus(String status)
    {
        //set target percent
        this.labelStatus.setText(status);
    }
}
