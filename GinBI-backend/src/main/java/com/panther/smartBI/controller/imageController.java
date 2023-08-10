package com.panther.smartBI.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.panther.smartBI.common.BaseResponse;
import com.panther.smartBI.common.ErrorCode;
import com.panther.smartBI.common.ResultUtils;
import com.panther.smartBI.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Gin 琴酒
 * @data 2023/8/7 15:32
 */
@RestController
@RequestMapping("/pics")
public class imageController {

    public static final String HOST = "https://cg-api.heyfriday.cn/style/feed/list";
    public static final Integer LIMITER = 20;

    @GetMapping
    public BaseResponse<List<String>> getAiPic(){
        String result = HttpRequest.get(HOST)
                .execute().body();
        if(StringUtils.isBlank(result)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据获取失败！");
        }
        String recode = JSONUtil.parseObj(result).getStr("result");
        JSONArray picArray = JSONUtil.parseArray(recode);
        if(picArray.size() == 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据获取失败！");
        }
        List<String> collect = picArray.stream().map((item -> {
            return JSONUtil.parseObj(item).getStr("icon");
        })).collect(Collectors.toList());

        return ResultUtils.success(collect);
    }

}
