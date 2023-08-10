package com.panther.smartBI.model.enums;

import org.apache.commons.lang3.ObjectUtils;

/**
 * @author Gin 琴酒
 * @data 2023/8/3 10:44
 */
public enum ChartStatusEnum {

    CHART_STATUS_WAITING("等待中",0),
    CHART_STATUS_RUNNING("生成中",2),
    CHART_STATUS_SUCCESS("成功",1),
    CHART_STATUS_FAILURE("失败",-1),
    ;

    private final String text;

    private final Integer value;

    ChartStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static ChartStatusEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (ChartStatusEnum anEnum : ChartStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
