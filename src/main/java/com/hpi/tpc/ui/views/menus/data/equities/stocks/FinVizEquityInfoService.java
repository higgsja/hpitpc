package com.hpi.tpc.ui.views.menus.data.equities.stocks;

import com.hpi.tpc.data.entities.*;
import java.util.*;
import java.util.stream.Stream;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@Component
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class FinVizEquityInfoService
{

    @Autowired private JdbcTemplate jdbcTemplate;

    public Stream<FinVizEquityInfoModel> fetchFinViz(int offset, int limit,
        String filterTicker, String filterSector, String filterIndustry, List<FinVizEquityInfoSort> sortModels)
    {
        List<FinVizEquityInfoModel> result = jdbcTemplate.query(
            String.format(FinVizEquityInfoModel.SQL_GET_LATEST_FILTERED,
                filterTicker.isEmpty() ? "%%" : filterTicker + "%",
                filterSector.isEmpty() ? "%%" : filterTicker + "%",
                filterIndustry.isEmpty() ? "%%" : filterTicker + "%",
                offset, limit),
            new FinVizMapper());

        return result.stream();
    }
}
