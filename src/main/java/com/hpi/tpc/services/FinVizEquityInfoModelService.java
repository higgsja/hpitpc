package com.hpi.tpc.services;

import com.hpi.tpc.ui.views.menus.data.equities.stocks.FilterFinVizInfo;
import com.hpi.tpc.data.entities.*;
import java.util.*;
import java.util.stream.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@Component
@NoArgsConstructor
public class FinVizEquityInfoModelService
{

    @Autowired private JdbcTemplate jdbcTemplate;

    public Stream<FinVizEquityInfoModel> findAll(Integer offset, Integer limit, Optional<FilterFinVizInfo> filter)
    {
        return this.jdbcTemplate.query(String.format(FinVizEquityInfoModel.SQL_GET_LATEST_FILTERED,
            filter.get().getFilterTicker(),
            filter.get().getFilterTicker(),
            filter.get().getFilterSector(),
            filter.get().getFilterSector(),
            filter.get().getFilterIndustry(),
            filter.get().getFilterIndustry(),
            offset, limit),new FinVizMapper()).stream();
    }

    public Integer findAllCount(Integer offset, Integer limit, Optional<FilterFinVizInfo> filter)
    {
        var rows = 0;

        var rs = jdbcTemplate.queryForRowSet(String.format(FinVizEquityInfoModel.SQL_GET_LATEST_FILTERED_COUNT,
            filter.get().getFilterTicker(),
            filter.get().getFilterTicker(),
            filter.get().getFilterSector(),
            filter.get().getFilterSector(),
            filter.get().getFilterIndustry(),
            filter.get().getFilterIndustry()));

        while (rs.next())
        {
            rows = rs.getInt("count(*)");
        }

        return rows;
    }
}
