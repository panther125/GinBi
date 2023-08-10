package com.panther.smartBI.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图表信息表(Chart)表实体类
 *
 * @author makejava
 * @since 2023-07-29 21:28:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chart extends Model<Chart> {
    //id
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //分析目标
    private String goal;
    /**
     * 名称
     */
    private String name;
    //图表信息
    private String chartData;
    //图表类型
    private String chartType;
    /**
     * 图表状态 0: wait-等待, 2: running-生成中,1:succeed-成功生成,-1:failed-生成失败
     */
    private Integer Status;
    /**
     * 执行信息
     */
    private String execMessage;
    //生成的图表信息
    private String genChart;
    //生成的分析结论
    private String genResult;
    //创建图标用户 id
    private Long userId;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //是否删除
    @TableLogic
    private Integer isDelete;
}

