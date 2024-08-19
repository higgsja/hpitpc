package com.hpi.tpc.data.entities;

import com.github.appreciated.apexcharts.helper.*;
import java.sql.Date;
import java.time.*;
import java.time.format.*;
import java.util.*;
import lombok.*;

@Getter @Setter
@Builder
@AllArgsConstructor
public class OHLCVModel {
//    @Builder.Default private List<OHLCVModel> ohlcvModels = null;
//    @Builder.Default private Series<Coordinate> ohlcvSeries = null;
    private static final String SQL_DAILY;
    private final String ticker;
    private final Date date;
    private final Double open;
    private final Double high;
    private final Double low;
    private final Double close;
    private final Double volume;

    static {
        SQL_DAILY =
            "select Ticker, `Date`, Open, High, Low, Close, Volume from EquityHistory where Ticker = '%s' and `Date` >= date_sub(now(), interval .5 year) order by `Date` asc";
    }

    public OHLCVModel(OHLCVModel ohlcvModel) {
        this(ohlcvModel.getTicker(), ohlcvModel.getDate(), ohlcvModel.getOpen(),
            ohlcvModel.getHigh(), ohlcvModel.getLow(), ohlcvModel.getClose(),
            ohlcvModel.getVolume());
    }

    public static String getSQL_DAILY() {
        return SQL_DAILY;
    }

    public static synchronized Series<Coordinate> getCoordSeries(
        String ticker, List<OHLCVModel> ohlcvModels) {
        Series seriesCoordinate;
        List<Coordinate> coordinates;

        coordinates = new ArrayList<>();

        for (OHLCVModel model : ohlcvModels) {
            //date, open, high, low, close
            coordinates.add(new Coordinate(model.date,
                model.open, model.high, model.low, model.close));
        }
        
        Coordinate[] data = new Coordinate[coordinates.size()];
        
        for (int i = 0; i < coordinates.size(); i++)
        {
            data[i] = coordinates.get(i);
        }

        seriesCoordinate = new Series();
        seriesCoordinate.setName(ticker);
        seriesCoordinate.setData(data);
        
        return seriesCoordinate;
    }

    public static synchronized String getISOString(long l) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.
            systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
