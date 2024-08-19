package com.hpi.tpc.ui.views.coaching.gains.chart;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.hpi.tpc.ui.views.coaching.gains.GainsVLModel;
import com.vaadin.flow.component.combobox.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@UIScope
@VaadinSessionScope
@Component
public class GainsControlsHL1
    extends ControlsHLBase
{
    @Autowired public GainsVLModel gainsVLModel;
    @Autowired public JdbcTemplate jdbcTemplate;

    //private GainsMVCView gainsMVCView = null;
    @Getter private final ComboBox<PositionModel> cbPositions = new ComboBox();
    @Getter private final ComboBox<TradeTacticModel> cbTactics = new ComboBox();
    @Getter private final ComboBox<TimeframeModel> cbTimeframe = new ComboBox();

    public GainsControlsHL1()
    {
        this.addClassName("gainsControlsHL1");
    }

    @PostConstruct
    public void construct_a()
    {
        //docs indicate abstract postConstruct will not be hit but it is
        int i = 1;
    }

    @Override
    public void doLayout()
    {
        this.setupTimeframe();
        this.setupPositions();
        this.setupTactics();

        this.add(this.cbPositions, this.cbTactics, this.cbTimeframe);
    }

    /**
     * setup methods are used on initialization of the page only
     */
    private void setupPositions()
    {
        this.cbPositions.setWidth("115px");

        //initial state: All positions, All tactics, YTD timeframe, All equityType
        this.setPositions((this.gainsVLModel.getSelectedTimeframeModel().getTimeframe().equalsIgnoreCase("Open")
            ? "PositionsOpen" : "PositionsClosed"), true);
    }

    /**
     * Come here because of a change in the position selection
     * just change to that selection
     * Come here because of a change elsewhere that causes a change in the position list
     * have to re-query for the right list that fits the criterion
     *
     * @param dataTable
     * @param bInit
     */
    protected void setPositions(String dataTable, Boolean bInit)
    {
        List<PositionModel> positions;
        List<PositionModel> tempPositionModels;

        positions = new ArrayList<>();

        positions.add(PositionModel.builder()
            .ticker("--All--")
            .build());

        //!! this is not using timeFrame
        //first time through gets zero records
        tempPositionModels = jdbcTemplate.query(String
            .format(PositionModel.POSITION_TACTIC_TIMEFRAME_EQUITYTYPE,
                dataTable, //table
                this.gainsVLModel.getSelectedPositionModel().getTicker(), //ticker
                this.gainsVLModel.getSelectedPositionModel().getTicker(), //ticker
                this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(), //tacticId
                this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(), //tacticId
                this.gainsVLModel.getSqlTimeframe(), //timeframe
                this.gainsVLModel.getSelectedEquityTypeModel().getEquityType(), //equityType
                this.gainsVLModel.getSelectedEquityTypeModel().getEquityType(), //equityType
                SecurityUtils.getUserId()),
            new PositionMapper2());

        //put the rest of equityTypes into the array
        for (PositionModel pm : tempPositionModels)
        {
            positions.add(pm);
        }

        if (bInit)
        {
            this.gainsVLModel.setPositionModels(positions);

            //set the combobox content
            this.cbPositions.setItems(this.gainsVLModel.getPositionModels());

            this.cbPositions.setValue(this.gainsVLModel.getPositionModels().get(0));
            this.gainsVLModel.setSelectedPositionModel(this.gainsVLModel.getPositionModels().get(0));
        } else
        {
            //odd behavior in combobox if we switch to a new list that is the same as the old list
            //  do not do that
            PositionModel[] array1 = positions.toArray(new PositionModel[0]);
            PositionModel[] array2 = this.gainsVLModel.getPositionModels().toArray(new PositionModel[0]);
            if (!Arrays.equals(array1, array2))
            {
                this.gainsVLModel.setPositionModels(positions);
                //change pick list
                this.cbPositions.setItems(this.gainsVLModel.getPositionModels());
                this.cbPositions.setValue(this.gainsVLModel.getSelectedPositionModel());
            }
        }
    }

    /**
     * reset the combobox to tactics that exist given initial selections
     * set the cursor on the first tactic
     */
    private void setupTactics()
    {
        this.cbTactics.setWidth("115px");

        this.setTactics((this.gainsVLModel.getSelectedTimeframeModel().getTimeframe().equalsIgnoreCase("Open")
            ? "PositionsOpen" : "PositionsClosed"), true);
    }

    /**
     * actions on selecting a different tactic
     * reset ticker list to those with this tactic plus other current selections
     * reset tactics list to this tactic
     * set tactics selection to this one
     *
     * @param dataTable
     * @param bInit
     */
    protected void setTactics(String dataTable, Boolean bInit)
    {
        List<TradeTacticModel> tactics;
        List<TradeTacticModel> tempTactics;

        tactics = new ArrayList<>();

        //add the All choice
        tactics.add(TradeTacticModel.builder()
            .tacticId(-1)
            .tacticName("--All--")
            .build());

        //not using timeframe
        tempTactics = jdbcTemplate.query(String.format(TradeTacticModel.TACTICS,
            dataTable,
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSqlTimeframe(), //timeframe
            this.gainsVLModel.getSelectedEquityTypeModel().getEquityType(), //equityType
            this.gainsVLModel.getSelectedEquityTypeModel().getEquityType(), //equityType
            SecurityUtils.getUserId()),
            new TradeTacticMapper());

        //add tradeTactics to the array
        for (TradeTacticModel ttm : tempTactics)
        {
            tactics.add(ttm);
        }

        if (bInit)
        {
            //set array content
            this.gainsVLModel.setTradeTacticModels(tactics);

            // set combobox content        
            this.cbTactics.setItems(tactics);

            this.cbTactics.setValue(this.gainsVLModel.getTradeTacticModels().get(0));
            this.gainsVLModel.setSelectedTradeTacticModel(this.gainsVLModel.getTradeTacticModels().get(0));
        } else
        {
            //odd behavior in combobox if we switch to a new list that is the same as the old list
            //  do not do that
            TradeTacticModel[] array1 = tactics.toArray(new TradeTacticModel[0]);
            TradeTacticModel[] array2 = this.gainsVLModel.getTradeTacticModels().toArray(new TradeTacticModel[0]);
            if (!Arrays.equals(array1, array2))
            {
                this.gainsVLModel.setTradeTacticModels(tactics);
                //change pick list
                this.cbTactics.setItems(this.gainsVLModel.getTradeTacticModels());
                this.cbTactics.setValue(this.gainsVLModel.getSelectedTradeTacticModel());
            }
        }
    }

    private void setupTimeframe()
    {
        this.cbTimeframe.setWidth("148px");

        //open uses positinsOpen; all others use positionsClosed
        this.setTimeframe(true);
    }

    /**
     * Establish the list of available timeframes for the selection combo box
     *
     * @param bInit
     */
    protected void setTimeframe(Boolean bInit)
    {
        List<PositionModel> tmpPositions;
        ArrayList<TimeframeModel> tmpTimeframes;

        tmpTimeframes = new ArrayList<>();

        //add timeframes where data exists: multiple queries to see what
        //  transactions are available
        //todo: optimize to reduce queries: 
        //open
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsOpen",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_OPEN, //timeframe
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (tmpPositions.size() > 0)
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.OPEN)
                .build());
        }

        //year
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_YTD,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.YTD)
                .build());
        }

        //quarter
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_QUARTER,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.QUARTER)
                .build());
        }

        //month
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_MONTH,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.MONTH)
                .build());
        }

        //week
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_WEEK,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.WEEK)
                .build());
        }

        //last year
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_LAST_YEAR,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.LAST_YEAR)
                .build());
        }

        //last quarter
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_LAST_QUARTER,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.LAST_QUARTER)
                .build());
        }

        //last month
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_LAST_MONTH,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.LAST_MONTH)
                .build());
        }

        //last week
        tmpPositions = jdbcTemplate.query(String.format(TimeframeModel.SQL_TIMEFRAMES,
            "PositionsClosed",
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedPositionModel().getTicker(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            this.gainsVLModel.getSelectedTradeTacticModel().getTacticId(),
            PositionModel.SQL_LAST_WEEK,
            SecurityUtils.getUserId()), //all equityType
            new PositionMapper2());

        if (!tmpPositions.isEmpty())
        {
            tmpTimeframes.add(TimeframeModel.builder()
                .timeframe(TimeframeModel.LAST_WEEK)
                .build());
        }

        if (bInit)
        {
            this.gainsVLModel.setTimeframes(tmpTimeframes);

            this.cbTimeframe.setItems(this.gainsVLModel.getTimeframes());
            this.cbTimeframe.setValue(this.gainsVLModel.getSelectedTimeframeModel());
        } else
        {
            //odd behavior in combobox if we switch to a new list that is the same as the old list
            //  do not do that
            TimeframeModel[] array1 = tmpTimeframes.toArray(new TimeframeModel[0]);
            TimeframeModel[] array2 = this.gainsVLModel.getTimeframes().toArray(new TimeframeModel[0]);
            if (!Arrays.equals(array1, array2))
            {
                this.gainsVLModel.setTimeframes(tmpTimeframes);
                //change pick list
                this.cbTimeframe.setItems(this.gainsVLModel.getTimeframes());
                this.cbTimeframe.setValue(this.gainsVLModel.getSelectedTimeframeModel());
            }
        }
    }
}
