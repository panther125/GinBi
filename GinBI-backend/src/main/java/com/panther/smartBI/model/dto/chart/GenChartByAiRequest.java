package com.panther.smartBI.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 */
@Data
public class GenChartByAiRequest implements Serializable {


    /**
     * 业务
     */
    private String goal;

    /**
     * 图表名称
     */
    private String name;

    private String chartType;

    private static final long serialVersionUID = 1L;
}