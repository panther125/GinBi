package com.panther.smartBI.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.panther.smartBI.model.dto.chart.ChartQueryRequest;
import com.panther.smartBI.model.dto.chart.GenChartByAiRequest;
import com.panther.smartBI.model.entity.Chart;
import com.panther.smartBI.model.vo.BiResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图表信息表(Chart)表服务接口
 *
 * @author makejava
 * @since 2023-07-29 21:27:53
 */
public interface ChartService extends IService<Chart> {

    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    BiResponse  getChartByAi(String csvData , GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);

    BiResponse  ByAiAsync(String csvData , GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);

    long saveRawData(String csvData , GenChartByAiRequest genChartByAiRequest, HttpServletRequest request);

    List<Long> getFailedChart();

    boolean  reloadChartByAi(long id ,HttpServletRequest request);
}

