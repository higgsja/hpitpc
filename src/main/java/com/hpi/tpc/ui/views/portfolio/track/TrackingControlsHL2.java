package com.hpi.tpc.ui.views.portfolio.track;

import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.component.checkbox.*;
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
public class TrackingControlsHL2
    extends ControlsHLBase
{

    @Autowired public TrackingMVC_M trackingMVCModel;
    @Autowired public JdbcTemplate jdbcTemplate;

    private final HorizontalLayout leftControlsTgtPctHL = new HorizontalLayout();
    @Getter private final NativeLabel leftControlsTgtPctLabel = new NativeLabel("9");
    private final HorizontalLayout leftControlsChkBxHL = new HorizontalLayout();
    @Getter private final Checkbox cbActiveOnly = new Checkbox("Active");
    @Getter private final Checkbox cbOpenOnly = new Checkbox("Open");

    @PostConstruct
    public void construct_a()
    {
        //docs indicate abstract postConstruct will not be hit but it is
        this.addClassName("trackingControlsHL2");
        this.setWidth("360px");
        this.setHeight("29px");
    }

    @Override
    public void doLayout()
    {
        setupSectorTgt();
        setupCheckBoxes();
    }

    private void setupSectorTgt()
    {
        this.leftControlsTgtPctHL.setClassName("trackingControlsSectorTgtPctHL");
        this.leftControlsTgtPctHL.setWidth("150px");

        //vertical alignment
        this.leftControlsTgtPctHL.setAlignItems(Alignment.CENTER);

        //add descr
        this.leftControlsTgtPctHL.add(new NativeLabel("Sector Tgt%: "));

        //add target percent
        this.leftControlsTgtPctHL.add(this.leftControlsTgtPctLabel);

        this.add(this.leftControlsTgtPctHL);

        this.setSectorTgt();
    }

    protected void setSectorTgt()
    {
        //set target percent
        this.leftControlsTgtPctLabel.setText(this.trackingMVCModel.getSelectedSectorTgtPct().toString());
    }

    private void setupCheckBoxes()
    {
        this.leftControlsChkBxHL.setClassName("trackingControlsChkBoxesHL");
        this.leftControlsChkBxHL.setWidth("120px");
        this.leftControlsChkBxHL.setAlignItems(Alignment.CENTER);
        
        this.leftControlsChkBxHL.add(this.cbOpenOnly, this.cbActiveOnly);

        this.add(this.leftControlsChkBxHL);

        this.setCheckBoxes();
    }

    private void setCheckBoxes()
    {
        this.cbActiveOnly.setValue(this.trackingMVCModel.getSelectedTrackActive());
        this.cbOpenOnly.setValue(this.trackingMVCModel.getSelectedTrackOpen());
    }
}
