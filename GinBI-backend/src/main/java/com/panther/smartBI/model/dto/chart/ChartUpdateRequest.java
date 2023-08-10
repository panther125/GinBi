package com.panther.smartBI.model.dto.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 */
@Data
public class ChartUpdateRequest implements Serializable {

    private Long id;
    //分析目标
    private String goal;
    /**
     * 图表名称
     */
    private String name;
    //图表类型
    private String chartType;

    // 上传图表用户id
    private Long userId;

    private static final long serialVersionUID = 1L;
}