package com.hpi.tpc.ui.views.coaching.gains.positions;

import com.hpi.tpc.data.entities.*;
import com.hpi.tpc.ui.views.baseClass.*;
import com.vaadin.flow.spring.annotation.*;
import java.time.*;
import java.time.temporal.*;
import static java.time.temporal.TemporalAdjusters.*;
import java.util.*;
import jakarta.annotation.*;

@UIScope
@VaadinSessionScope
@org.springframework.stereotype.Component
public class GainsPositionGridVL
    extends PositionGridVLBase
{

    public GainsPositionGridVL()
    {
        this.addClassName("coachingPositionsGridVL");
        this.setWidth("100%");
        this.setHeight("100%");
        this.getStyle().set("padding", "0px 0px 16px 0px");
    }

    @PostConstruct
    private void init()
    {
        
    }

    //filter positions grid based on client selections
    public void filterPositions(String ticker, Integer tradeTactic, String timeframe, String equityType)
    {
        java.sql.Date yearFirstDay;

        yearFirstDay = java.sql.Date.valueOf(LocalDate.now().with(firstDayOfYear()));

        this.getDataProvider().clearFilters();

        //ticker filter
        if (ticker != null && !ticker.equalsIgnoreCase("--All--"))
        {
            this.getDataProvider().addFilter(position -> position.getTicker().equalsIgnoreCase(ticker));
        }

        //tactic filter
        if (tradeTactic != null && tradeTactic != -1)
        {
            this.getDataProvider().addFilter(position -> Objects.equals(position.getTacticId(), tradeTactic));
        }

        //timeframe filter
        if (timeframe != null && !timeframe.isEmpty())
        {
            switch (timeframe)
            {
                case TimeframeModel.OPEN:
                    this.getDataProvider().addFilter(position ->
                    {
                        return position.getDateClose() == null;
                    });
                    break;
                case TimeframeModel.LAST_WEEK:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            final LocalDate localDate = LocalDate.parse(position.getDateClose().toString());

                            return (localDate.compareTo(LocalDate.now(ZoneId.of("America/Chicago")).minusDays(7)
                                .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))) >= 0
                                && localDate.compareTo(LocalDate.now(ZoneId.of("America/Chicago"))
                                    .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY))) <= 0);
                        } else
                        {
                            return false;
                        }
                    });
                    break;
                case TimeframeModel.LAST_MONTH:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            final LocalDate localDate = LocalDate.parse(position.getDateClose().toString());
                            return localDate.compareTo((LocalDate.now(ZoneId.of("America/Chicago")))
                                .minusMonths(1).with(TemporalAdjusters.firstDayOfMonth())) >= 0
                                && localDate.compareTo((LocalDate.now(ZoneId.of("America/Chicago")))
                                    .minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())) <= 0;
                        } else
                        {
                            return false;
                        }
                    });
                    break;
                case TimeframeModel.LAST_QUARTER:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            final LocalDate localDate = LocalDate.parse(position.getDateClose().toString());
                            return localDate.compareTo((LocalDate.now(ZoneId.of("America/Chicago"))
                                .minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()))) >= 0
                                && localDate.compareTo((LocalDate.now(ZoneId.of("America/Chicago"))
                                    .minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()))) <= 0;
                        } else
                        {
                            return false;
                        }
                    });
                    break;
                case TimeframeModel.LAST_YEAR:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            final LocalDate localDate = LocalDate.parse(position.getDateClose().toString());
                            return localDate.compareTo((LocalDate.now(ZoneId.of("America/Chicago")).minusYears(1)
                                .with(TemporalAdjusters.firstDayOfYear()))) >= 0
                                && localDate.compareTo((LocalDate.now(ZoneId.of("America/Chicago"))
                                    .minusYears(1).with(TemporalAdjusters.lastDayOfYear()))) <= 0;
                        } else
                        {
                            return false;
                        }
                    });
                    break;
                case TimeframeModel.WEEK:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            //closed position
                            return position.getDateClose().toString().
                                compareTo(LocalDate.now(ZoneId.of("America/Chicago"))
                                    .with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).toString()) >= 0;
                        } else
                        {
                            //open position
                            return false;
                        }
                    });
                    break;
                case TimeframeModel.MONTH:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            final LocalDate localDate = LocalDate.parse(position.getDateClose().toString());
                            //closed position
                            return localDate.compareTo(LocalDate.now(ZoneId.of("America/Chicago"))
                                .with(TemporalAdjusters.firstDayOfMonth())) >= 0;
                        } else
                        {
                            //open position
                            return false;
                        }
                    });
                    break;
                case TimeframeModel.QUARTER:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            return position.getDateClose().toString().compareTo(LocalDate
                                .now(ZoneId.of("America/Chicago"))
                                .with((LocalDate.now()).getMonth().firstMonthOfQuarter())
                                .with(TemporalAdjusters.firstDayOfMonth()).toString()) >= 0
                                && position.getDateClose().toString().compareTo(LocalDate
                                    .now(ZoneId.of("America/Chicago"))
                                    .with((LocalDate.now()).getMonth().firstMonthOfQuarter()).plusMonths(2)
                                    .with(TemporalAdjusters.lastDayOfMonth()).toString()) <= 0;
                        } else
                        {
                            //open position
                            return false;
                        }
                    });
                    break;
                case TimeframeModel.YTD:
                    this.getDataProvider().addFilter(position ->
                    {
                        if (position.getDateClose() != null)
                        {
                            //closed position
                            return position.getDateClose().compareTo(yearFirstDay) >= 0;
                        } else
                        {
                            //open position
                            return false;
                        }
                    });
                    break;
                default:
                    int z = 0;
            }
        }

        //equityType filter
        if (equityType != null && !equityType.equalsIgnoreCase("--All--"))
        {
            this.getDataProvider().addFilter(position
                -> equityType.equalsIgnoreCase(position.getEquityType()));
        }
    }
}
