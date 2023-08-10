package com.panther.smartBI.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.panther.smartBI.annotation.AuthCheck;
import com.panther.smartBI.annotation.RateCount;
import com.panther.smartBI.bizMQ.SendMessage;
import com.panther.smartBI.common.BaseResponse;
import com.panther.smartBI.common.DeleteRequest;
import com.panther.smartBI.common.ErrorCode;
import com.panther.smartBI.common.ResultUtils;
import com.panther.smartBI.constant.BiConstant;
import com.panther.smartBI.constant.FileConstant;
import com.panther.smartBI.constant.UserConstant;
import com.panther.smartBI.exception.BusinessException;
import com.panther.smartBI.exception.ThrowUtils;
import com.panther.smartBI.manager.RedissonLimiterManager;
import com.panther.smartBI.model.dto.chart.*;
import com.panther.smartBI.model.entity.Chart;
import com.panther.smartBI.model.entity.User;
import com.panther.smartBI.model.vo.BiResponse;
import com.panther.smartBI.service.ChartService;
import com.panther.smartBI.service.UserService;
import com.panther.smartBI.utils.ExcelUtils;
import com.panther.smartBI.utils.UserInputUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图表接口
 *
 */
@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private SendMessage sendMessage;


    // region 增删改查

    /**
     * 创建
     *
     * @param chartAddRequest 添加图表请求
     * @param request 原生请求类
     * @return 返回添加图标后的id
     */
    @PostMapping("/add")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartAddRequest, chart);
        User loginUser = userService.getLoginUser(request);
        chart.setUserId(loginUser.getId());
        boolean result = chartService.save(chart);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chart.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Chart oldchart = chartService.getById(id);
        ThrowUtils.throwIf(oldchart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldchart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest 更新图标请求类
     * @return 统一返回
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartUpdateRequest, chart);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        Chart oldchart = chartService.getById(id);
        ThrowUtils.throwIf(oldchart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id 文章id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<Chart> getChartVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /**
     * 因为文件解析多次使用，所以将改代码抽离
     *
     * @param multipartFile 源文件
     * @return 返回 csv 字符串
     */
    public String analysisFile(MultipartFile multipartFile){
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR, "文件为空");
        //校验文件
        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > FileConstant.MAX_FILE_SIZE, ErrorCode.SYSTEM_ERROR, "文件超过1M");
        String originalFilename = multipartFile.getOriginalFilename();
        String fileSuffix = FileUtil.getSuffix(originalFilename);
        ThrowUtils.throwIf(!BiConstant.VALID_FILE_SUFFIX_LIST.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "文件格式有误");
        return ExcelUtils.ExcelToCsv(multipartFile,fileSuffix);
    }

    /**
     * 智能分析图标
     *
     * @param multipartFile 用户上传的文件
     * @param genChartByAiRequest 用户的请求参数
     * @param request 原生 request 请求
     * @return 统一返回
     */
    @PostMapping("/gen")
    @RateCount(count = "2")
    public BaseResponse<BiResponse> getChartByAi(@RequestPart("file") MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        //参数校验
        String csvData = analysisFile(multipartFile);
        BiResponse biResponse = chartService.getChartByAi(csvData, genChartByAiRequest, request);
        return ResultUtils.success(biResponse);
    }

    @PostMapping("/gen/async")
    @RateCount
    public BaseResponse<BiResponse> ByAiAsync(@RequestPart("file") MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String csvData = analysisFile(multipartFile);
        BiResponse biResponse = chartService.ByAiAsync(csvData, genChartByAiRequest, request);
        return ResultUtils.success(biResponse);
    }

    @PostMapping("/gen/async/mq")
    @RateCount
    public BaseResponse<BiResponse> ByAiAsyncMQ(@RequestPart("file") MultipartFile multipartFile, GenChartByAiRequest genChartByAiRequest, HttpServletRequest request) {
        String csvData = analysisFile(multipartFile);
        long chartId = chartService.saveRawData(csvData, genChartByAiRequest, request);
        sendMessage.sendMessage(String.valueOf(chartId));
        BiResponse biResponse = new BiResponse();
        biResponse.setChartId(chartId);
        return ResultUtils.success(biResponse);
    }

    @GetMapping("/reload/gen")
    @RateCount(count = "10")
    public BaseResponse<Boolean> reloadChartByAi(long chartId, HttpServletRequest request) {
        return ResultUtils.success(chartService.reloadChartByAi(chartId, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest 用户请求参数分装类
     * @param request 原生 request 请求
     * @return 统一返回
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Chart>> listChartVOByPage(@RequestBody ChartQueryRequest chartQueryRequest,
            HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public BaseResponse<Page<Chart>> listMyChartVOByPage(@RequestBody ChartQueryRequest chartQueryRequest,
            HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Chart> chartPage = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(chartPage);
    }
    /**
     * 编辑（用户）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = new Chart();
        BeanUtils.copyProperties(chartEditRequest, chart);
        User loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // 判断是否存在
        Chart oldchart = chartService.getById(id);
        ThrowUtils.throwIf(oldchart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldchart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chart);
        return ResultUtils.success(result);
    }

}
