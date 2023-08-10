package com.panther.smartBI.model.vo;

import lombok.Data;

/**
 * BI 生成结果返回
 *
 * @author Gin 琴酒
 * @data 2023/7/30 17:11
 */
@Data
public class BiResponse {

    /**
     * 图表id
     */
    private Long chartId;

    /**
     * 生成的图表数据
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;

    /**
     * 生成状态[0:等待1:运行中2:失败3:成功]
     */
    private Integer genStatus;

    /**
     * 执行信息
     */
    private String execMessage;

}
