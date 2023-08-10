package com.panther.smartBI.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel 通用处理数据
 *
 * @author Gin 琴酒
 * @data 2023/7/30 10:49
 */
@Slf4j
public class ExcelUtils {

    public static String ExcelToCsv(MultipartFile multipartFile,String fileSuffix) {

        List<Map<Integer, String>> list = null;
        ExcelTypeEnum fileType = ExcelTypeEnum.CSV;
        fileSuffix = "."+fileSuffix;
        ExcelTypeEnum[] values = ExcelTypeEnum.values();
        for(ExcelTypeEnum item : values){
            if(item.getValue().equals(fileSuffix)){
                fileType = item;
                break;
            }
        }
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(fileType)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("文件处理错误:{}",e.getMessage());
            throw new RuntimeException(e);
        }

        if(CollUtil.isEmpty(list)){
            return "";
        }
        StringBuilder result = new StringBuilder();
        // 处理表头
        LinkedHashMap<Integer, String> headMap = (LinkedHashMap)list.get(0);

        List<String> head = headMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        result.append(StringUtils.join(head, ",")).append("\n");
        // 处理数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap)list.get(i);
            List<String> data = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            result.append(StringUtils.join(data, ",")).append("\n");
        }
        return result.toString();
    }

}
