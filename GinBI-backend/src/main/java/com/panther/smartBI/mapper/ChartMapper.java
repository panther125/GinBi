package com.panther.smartBI.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.panther.smartBI.model.entity.Chart;

import java.util.List;

/**
 * 图表信息表(Chart)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-29 21:27:52
 */
public interface ChartMapper extends BaseMapper<Chart> {

    List<Long> getFailedChart();

}

