package com.hpi.tpc.ui.views.coaching.benchmark;

import com.github.appreciated.apexcharts.*;
import com.hpi.tpc.charts.*;
import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.spring.annotation.*;
import java.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
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
@NoArgsConstructor
@Getter
public class BenchmarkModel
    extends MVCModelBase
{

    @Autowired private BenchmarkService benchmarkService;

    private LinesChart linesChart;

    public ApexCharts doChart()
    {
        TimeseriesModel aTimeseriesModel;
        List<TimeseriesModel> timeseriesModelsAcctTotals;
        List<TimeseriesModel> timeseriesModelsSpy;
        List<TimeseriesModel> timeseriesModelsIWM;
        List<TimeseriesModel> timeseriesModelsDIA;
        List<TimeseriesModel> timeseriesModelsQQQ;
        List<String> colorList;

        colorList = new ArrayList<>();
        colorList.add("#16A65E");

        String[] colorStrings =
        {
            "#00FF00", "#FF0000", "#0183FF", "#FC00FF", "#E8FF00"
        };

//        userId = SecurityUtils.getUserId().toString();
//        timeseriesModelsAcctTotals = serviceTPC.getTimeseriesModels(
//            String.format(TimeseriesModel.SQL_ACCT_TOTALS, userId, userId,
//                userId, userId, userId));
        timeseriesModelsAcctTotals = new ArrayList<>();

        aTimeseriesModel = TimeseriesModel.builder()
            .ticker("Portfolio")
            .date(new Date())
            .data(0.0)
            .build();
        timeseriesModelsAcctTotals.add(aTimeseriesModel);

        timeseriesModelsSpy = benchmarkService.getTimeseriesModels(
            String.format(TimeseriesModel.SQL_GAINPCT, "SPY", "SPY", "SPY",
                "SPY", "SPY", "SPY"));

        timeseriesModelsIWM = benchmarkService.getTimeseriesModels(
            String.format(TimeseriesModel.SQL_GAINPCT, "IWM", "IWM", "IWM",
                "IWM", "IWM", "IWM"));

        timeseriesModelsDIA = benchmarkService.getTimeseriesModels(
            String.format(TimeseriesModel.SQL_GAINPCT, "DIA", "DIA", "DIA",
                "DIA", "DIA", "DIA"));

        timeseriesModelsQQQ = benchmarkService.getTimeseriesModels(
            String.format(TimeseriesModel.SQL_GAINPCT, "QQQ", "QQQ", "QQQ",
                "QQQ", "QQQ", "QQQ"));

        this.linesChart = new LinesChart(colorStrings, colorList,
            TimeseriesModel.getCoordSeries("Portfolio", timeseriesModelsAcctTotals),
            TimeseriesModel.getCoordSeries("S&P", timeseriesModelsSpy),
            TimeseriesModel.getCoordSeries("IWM", timeseriesModelsIWM),
            TimeseriesModel.getCoordSeries("DIA", timeseriesModelsDIA),
            TimeseriesModel.getCoordSeries("QQQ", timeseriesModelsQQQ));

        return linesChart.getChart();
    }

    @Override
    public void getPrefs(String prefix)
    {
        //hit
        int i = 0;
//        this.prefsController.setDefaults("PortfolioTrack");
//        this.prefsController.readPrefsByPrefix("PortfolioTrack");
//        this.selectedTrackActive = this.prefsController
//            .getPref("PortfolioTrackActive").equals("Yes");
//        this.selectedTrackOpen = this.prefsController
//            .getPref("PortfolioTrackOpen").equals("Yes");
    }

    @Override
    public void writePrefs(String prefix)
    {
        //hit
        int i = 0;
        //preferences, update the hashmap, then write to database
//        this.prefsController.setPref("PortfolioTrackActive",
//            selectedTrackActive ? "Yes" : "No");
//        this.prefsController.setPref("PortfolioTrackOpen",
//            selectedTrackOpen ? "Yes" : "No");
//        
//        this.prefsController.writePrefsByPrefix("PortfolioTrack");
    }
}
