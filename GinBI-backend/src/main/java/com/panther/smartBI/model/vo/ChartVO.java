package com.panther.smartBI.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Gin 琴酒
 * @data 2023/7/30 17:13
 */
@Data
public class ChartVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图标名称
     */
    private String chartName;

    /**
     * 图标数据
     */
    private String chartData;

    /**
     * 图标类型
     */
    private String chartType;

    /**
     * 生成的图表数据
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 生成状态[0:等待1:运行中2:失败3:成功]
     */
    private Integer genStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}

