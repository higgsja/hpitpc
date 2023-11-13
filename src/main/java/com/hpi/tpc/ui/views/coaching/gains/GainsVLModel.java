package com.hpi.tpc.ui.views.coaching.gains;

import com.hpi.tpc.app.security.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import jakarta.annotation.*;
import lombok.*;
import org.springframework.stereotype.*;

/**
 * handles state of application
 * contains objects representing the data
 * provides ways to query and change the data
 * responds to requests from View and instructions from Controller
 */
@UIScope
@VaadinSessionScope
@Component
@Getter @Setter
public class GainsVLModel
    extends MVCModelBase
{

    private PositionModel selectedPositionModel;
    private TradeTacticModel selectedTradeTacticModel;
    private TimeframeModel selectedTimeframeModel;
    private String sqlTimeframe;
    private EquityTypeModel selectedEquityTypeModel;
    private String selectedChartType;

    private Boolean bInSelections = false;

    private List<PositionModel> positionModels;
    private List<TradeTacticModel> tradeTacticModels;
    private List<EquityTypeModel> equityTypeModels;
    private ArrayList<TimeframeModel> timeframes;

    private List<GainModel> gainsModels;
    private Double gainsTotal;
    private Double chartPctMin;
    private Double chartMin;
    private Double chartPctMax;
    private Double chartMax;

//    private TornadoChart chart;

    public GainsVLModel()
    {
        this.gainsTotal = 0.0;
        this.chartPctMin = 0.0;
        this.chartMin = 0.0;
        this.chartPctMax = 0.0;
        this.chartMax = 0.0;

        this.doInitSelections();
    }

    @PostConstruct
    private void construct()
    {
    }

    public void setSelectedTimeframeModel(TimeframeModel tfm)
    {
        this.selectedTimeframeModel = tfm;

        switch (this.selectedTimeframeModel.getTimeframe())
        {
            case TimeframeModel.OPEN:
                sqlTimeframe = PositionModel.SQL_OPEN;
                break;
            case TimeframeModel.LAST_YEAR:
                sqlTimeframe = PositionModel.SQL_LAST_YEAR;
                break;
            case TimeframeModel.LAST_QUARTER:
                sqlTimeframe = PositionModel.SQL_LAST_QUARTER;
                break;
            case TimeframeModel.LAST_MONTH:
                sqlTimeframe = PositionModel.SQL_LAST_MONTH;
                break;
            case TimeframeModel.LAST_WEEK:
                sqlTimeframe = PositionModel.SQL_LAST_WEEK;
                break;
            case TimeframeModel.WEEK:
                sqlTimeframe = PositionModel.SQL_WEEK;
                break;
            case TimeframeModel.MONTH:
                sqlTimeframe = PositionModel.SQL_MONTH;
                break;
            case TimeframeModel.QUARTER:
                sqlTimeframe = PositionModel.SQL_QUARTER;
                break;
            case TimeframeModel.YTD:
                sqlTimeframe = PositionModel.SQL_YTD;
                break;
            default:
                sqlTimeframe = PositionModel.SQL_YTD;
        }
    }

    /**
     * establish data elements for controls
     */
    private void doInitSelections()
    {
        this.setSelectedPositionModel(PositionModel.builder()
            .ticker("--All--")
            .build());
        this.setSelectedTradeTacticModel(TradeTacticModel.builder()
            .tacticId(-1)
            .tacticName("--All--")
            .build());
        this.setSelectedTimeframeModel(TimeframeModel.builder()
            .timeframe(TimeframeModel.YTD)
            .build());
        this.setSqlTimeframe(PositionModel.SQL_YTD);
        this.setSelectedEquityTypeModel(EquityTypeModel.builder()
            .equityType("--All--")
            .build());
        this.setSelectedChartType("$");
    }

    public void getChartData(String dataTable)
    {
        //special case: all positions, all tactics, any timeframe, all equityTypes, make category position
        if (this.selectedPositionModel.toString().equalsIgnoreCase("--All--")
            && this.selectedTradeTacticModel.getTacticName().equalsIgnoreCase("--All--"))
        {
            this.gainsModels = jdbcTemplate.query(String.format(GainModel.SQL_GAINS_BY_POSITION,
                dataTable,
                this.selectedPositionModel.getTicker(),
                this.selectedPositionModel.getTicker(),
                this.selectedTradeTacticModel.getTacticId(),
                this.selectedTradeTacticModel.getTacticId(),
                this.sqlTimeframe,
                this.selectedEquityTypeModel,
                this.selectedEquityTypeModel,
                SecurityUtils.getUserId(),
                this.selectedChartType),
                new GainMapper());
        } else
        {
            this.gainsModels = jdbcTemplate.query(String.format(GainModel.SQL_GAINS_BY_TACTIC,
                dataTable,
                this.selectedPositionModel.getTicker(),
                this.selectedPositionModel.getTicker(),
                this.selectedTradeTacticModel.getTacticId(),
                this.selectedTradeTacticModel.getTacticId(),
                this.sqlTimeframe,
                this.selectedEquityTypeModel,
                this.selectedEquityTypeModel,
                SecurityUtils.getUserId(),
                this.selectedChartType),
                new GainMapper());
        }

        if (!gainsModels.isEmpty())
        {
            this.gainsTotal = 0.0;
            this.chartMax = gainsModels.get(0).getGain();
            this.chartPctMax = gainsModels.get(0).getGainPct();
            this.chartMin = gainsModels.get(gainsModels.size() - 1).getGain();
            this.chartPctMin = gainsModels.get(gainsModels.size() - 1).getGainPct();
            for (GainModel gm : gainsModels)
            {
                this.gainsTotal += gm.getGain();
            }

            this.positionModels = jdbcTemplate.query(
                String.format(TimeframeModel.SQL_TIMEFRAMES,
                    "PositionsClosed",
                    this.selectedPositionModel.getTicker(),
                    this.selectedPositionModel.getTicker(),
                    this.selectedTradeTacticModel.getTacticId(),
                    this.selectedTradeTacticModel.getTacticId(),
                    sqlTimeframe,
                    SecurityUtils.getUserId()), //all equityType
                new PositionMapper2());
        }
    }

    @Override
    public void getPrefs(String prefix)
    {
//        this.prefsController.readPrefsByPrefix("CoachingGains");
//        this.selectedTrackActive = this.prefsController
//            .getPref("PortfolioTrackActive").equals("Yes");
//        this.selectedTrackOpen = this.prefsController
//            .getPref("PortfolioTrackOpen").equals("Yes");
    }

    @Override
    public void writePrefs(String prefix)
    {
        //preferences, update the hashmap, then write to database
//        this.prefsController.setPref("PortfolioTrackActive",
//            selectedTrackActive ? "Yes" : "No");
//        this.prefsController.setPref("PortfolioTrackOpen",
//            selectedTrackOpen ? "Yes" : "No");
//        
//        this.prefsController.writePrefsByPrefix("PortfolioTrack");
    }
}
