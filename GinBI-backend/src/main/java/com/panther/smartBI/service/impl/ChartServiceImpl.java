package com.panther.smartBI.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.panther.smartBI.common.ErrorCode;
import com.panther.smartBI.constant.BiConstant;
import com.panther.smartBI.constant.CommonConstant;
import com.panther.smartBI.exception.BusinessException;
import com.panther.smartBI.exception.ThrowUtils;
import com.panther.smartBI.manager.AiManager;
import com.panther.smartBI.mapper.ChartMapper;
import com.panther.smartBI.model.dto.chart.ChartQueryRequest;
import com.panther.smartBI.model.dto.chart.GenChartByAiRequest;
import com.panther.smartBI.model.entity.Chart;
import com.panther.smartBI.model.entity.User;
import com.panther.smartBI.model.enums.ChartStatusEnum;
import com.panther.smartBI.model.vo.BiResponse;
import com.panther.smartBI.service.ChartService;
import com.panther.smartBI.service.UserService;
import com.panther.smartBI.utils.SqlUtils;
import com.panther.smartBI.utils.UserInputUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 图表信息表(Chart)表服务实现类
 *
 * @author makejava
 * @since 2023-07-29 21:27:53
 */
@Service("chartService")
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {

    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;

    @Resource
    private ThreadPoolExecutor theadPoolExecutor;

    /**
     * 获取查询包装类
     *
     * @param chartQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chartQueryRequest.getId();
        String chartType = chartQueryRequest.getChartType();
        String goal = chartQueryRequest.getGoal();
        Long userId = chartQueryRequest.getUserId();
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();
        String chartName = chartQueryRequest.getName();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "userId", id);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.eq(userId != null && userId > 0, "userId", userId);
        //queryWrapper.eq("isDelete", 0);
        queryWrapper.like(StringUtils.isNotBlank(chartName), "name", chartName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return queryWrapper;
    }

    @Override
    public BiResponse getChartByAi(String csvData, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String goal = genChartByAiRequest.getGoal();
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "分析目标为空");
        String chartName = genChartByAiRequest.getName();
        ThrowUtils.throwIf(StringUtils.isBlank(chartName) && chartName.length() > 100, ErrorCode.PARAMS_ERROR, "图表名称为空");
        String chartType = genChartByAiRequest.getChartType();

        //获取登录用户
        User loginUser = userService.getLoginUser(request);
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
        //向AI提问
        String aiRes = aiManager.doChat(BiConstant.BI_MODEL_ID_S, userInput.toString());
        //截取AI数据
        final String str = "=>=>=>";
        String[] aiData = aiRes.split(str);
        //log.info("aiData len = {} data = {}", aiData.length, aiRes);
        ThrowUtils.throwIf(aiData.length < 3, ErrorCode.SYSTEM_ERROR, "AI生成错误");
        String genChart = aiData[1].trim();
        String genResult = aiData[2].trim();
        // 判断用户积分是否用光
        boolean b = userService.updateUserChartCount(request);
        ThrowUtils.throwIf(!b,ErrorCode.FORBIDDEN_ERROR,"次数用完请联系管理员！");
        //插入数据
        Chart chart = new Chart();
        chart.setGoal(goal);
        chart.setName(chartName);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setGenChart(genChart);
        chart.setGenResult(genResult);
        chart.setUserId(loginUser.getId());
        chart.setStatus(ChartStatusEnum.CHART_STATUS_SUCCESS.getValue());
        chart.setStatus(ChartStatusEnum.CHART_STATUS_SUCCESS.getValue());
        chart.setExecMessage(ChartStatusEnum.CHART_STATUS_SUCCESS.getText());
        ThrowUtils.throwIf(!this.save(chart), ErrorCode.SYSTEM_ERROR, "图表保存失败");
        //返回AI对话数据
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        return biResponse;
    }

    @Override
    public BiResponse ByAiAsync(String csvData, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String goal = genChartByAiRequest.getGoal();
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "分析目标为空");
        String chartName = genChartByAiRequest.getName();
        ThrowUtils.throwIf(StringUtils.isBlank(chartName) && chartName.length() > 100, ErrorCode.PARAMS_ERROR, "图表名称为空");
        String chartType = genChartByAiRequest.getChartType();

        //获取登录用户
        User loginUser = userService.getLoginUser(request);
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
        //插入数据
        Chart chart = new Chart();
        chart.setGoal(goal);
        chart.setName(chartName);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setUserId(loginUser.getId());
        chart.setStatus(ChartStatusEnum.CHART_STATUS_WAITING.getValue());
        chart.setExecMessage(ChartStatusEnum.CHART_STATUS_WAITING.getText());
        ThrowUtils.throwIf(!this.save(chart), ErrorCode.SYSTEM_ERROR, "图表保存失败");
        CompletableFuture.runAsync(() -> {
            Chart update = new Chart();
            update.setId(chart.getId());
            // 更新状态为执行中
            update.setStatus(ChartStatusEnum.CHART_STATUS_RUNNING.getValue());
            update.setExecMessage(ChartStatusEnum.CHART_STATUS_RUNNING.getText());
            if (!this.updateById(update)) {
                // 如果队列已满 则执行失败
                update.setStatus(ChartStatusEnum.CHART_STATUS_FAILURE.getValue());
                update.setExecMessage(ChartStatusEnum.CHART_STATUS_FAILURE.getText());
                this.updateById(update);
            }
            String aiRes = aiManager.doChat(BiConstant.BI_MODEL_ID_S, userInput.toString());
            //截取AI数据
            final String str = "=>=>=>";
            String[] aiData = aiRes.split(str);
            String genChart = aiData[1].trim();
            String genResult = aiData[2].trim();
            //log.info("aiData len = {} data = {}", aiData.length, aiRes);
            if (StringUtils.isBlank(genChart) || StringUtils.isBlank(genResult)) {
                update.setStatus(ChartStatusEnum.CHART_STATUS_FAILURE.getValue());
                update.setExecMessage(ChartStatusEnum.CHART_STATUS_FAILURE.getText());
                updateById(update);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 分析失败!");
            }
            boolean b = userService.updateUserChartCount(request);
            ThrowUtils.throwIf(!b,ErrorCode.FORBIDDEN_ERROR,"次数用完请联系管理员！");
            // 更新状态为成功
            update.setStatus(ChartStatusEnum.CHART_STATUS_SUCCESS.getValue());
            update.setExecMessage(ChartStatusEnum.CHART_STATUS_SUCCESS.getText());
            update.setGenChart(genChart);
            update.setGenResult(genResult);
            this.updateById(update);
        }, theadPoolExecutor);
        //返回AI对话数据
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chart.getId());
        return biResponse;
    }

    @Override
    public long saveRawData(String csvData, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {

        String name = genChartByAiRequest.getName();
        String goal = genChartByAiRequest.getGoal();
        String chartType = genChartByAiRequest.getChartType();

        User loginUser = userService.getLoginUser(request);
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append(csvData).append("\n");
        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus(ChartStatusEnum.CHART_STATUS_WAITING.getValue());
        chart.setExecMessage(ChartStatusEnum.CHART_STATUS_WAITING.getText());
        chart.setUserId(loginUser.getId());
        ThrowUtils.throwIf(!this.save(chart), ErrorCode.SYSTEM_ERROR, "图表保存失败");
        return chart.getId();
    }

    @Override
    public List<Long> getFailedChart() {
        return baseMapper.getFailedChart();
    }

    @Override
    public boolean reloadChartByAi(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id < 0, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        //发送消息
        CompletableFuture.runAsync(() -> {
            Chart update = new Chart();
            update.setId(id);
            // 更新状态为执行中
            update.setStatus(ChartStatusEnum.CHART_STATUS_RUNNING.getValue());
            update.setExecMessage(ChartStatusEnum.CHART_STATUS_RUNNING.getText());
            if (!this.updateById(update)) {
                // 如果队列已满 则执行失败
                update.setStatus(ChartStatusEnum.CHART_STATUS_FAILURE.getValue());
                update.setExecMessage(ChartStatusEnum.CHART_STATUS_FAILURE.getText());
                this.updateById(update);
            }
            String userInput = UserInputUtils.BuilderUserInput(id, this);
            String aiRes = aiManager.doChat(BiConstant.BI_MODEL_ID_S, userInput.toString());
            //截取AI数据
            final String str = "=>=>=>";
            String[] aiData = aiRes.split(str);
            String genChart = aiData[1].trim();
            String genResult = aiData[2].trim();
            //log.info("aiData len = {} data = {}", aiData.length, aiRes);
            if (StringUtils.isBlank(genChart) || StringUtils.isBlank(genResult)) {
                update.setStatus(ChartStatusEnum.CHART_STATUS_FAILURE.getValue());
                update.setExecMessage(ChartStatusEnum.CHART_STATUS_FAILURE.getText());
                updateById(update);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 分析失败!");
            }
            boolean b = userService.updateUserChartCount(request);
            ThrowUtils.throwIf(!b,ErrorCode.FORBIDDEN_ERROR,"次数用完请联系管理员！");
            // 更新状态为成功
            update.setStatus(ChartStatusEnum.CHART_STATUS_SUCCESS.getValue());
            update.setExecMessage(ChartStatusEnum.CHART_STATUS_SUCCESS.getText());
            update.setGenChart(genChart);
            update.setGenResult(genResult);
            this.updateById(update);
        }, theadPoolExecutor);
        return true;
    }

}

