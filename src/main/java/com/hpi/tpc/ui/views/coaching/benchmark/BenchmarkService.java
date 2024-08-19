package com.hpi.tpc.ui.views.coaching.benchmark;

import com.hpi.tpc.data.entities.*;
import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;

@Repository
public class BenchmarkService
{
    @Autowired private JdbcTemplate jdbcTemplate;
    
    public synchronized List<TimeseriesModel> getTimeseriesModels(String sql)
    {
        List<TimeseriesModel> timeseriesModels;

        timeseriesModels = jdbcTemplate.query(sql, new TimeseriesMapper());

        return timeseriesModels;
    }
}
