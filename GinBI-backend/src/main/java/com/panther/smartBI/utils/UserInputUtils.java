package com.panther.smartBI.utils;

import com.panther.smartBI.model.entity.Chart;
import com.panther.smartBI.service.ChartService;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Gin 琴酒
 * @data 2023/8/6 16:10
 */
public class UserInputUtils {

    public static String BuilderUserInput(long chartId ,ChartService chartService){
        // 根据消息队列提供的 chart id 获取对应的 chart
        Chart chart = chartService.getById(chartId);
        if(chart == null){
            return null;
        }
        String goal = chart.getGoal();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        StringBuilder userInput = new StringBuilder();
        // 添加分析目标
        userInput.append("分析需求：").append("\n");
        //拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append(goal).append("\n");
        userInput.append("原始数据：").append("\n");
        //csv数据

        userInput.append(csvData).append("\n");

        return userInput.toString();
    }

}
