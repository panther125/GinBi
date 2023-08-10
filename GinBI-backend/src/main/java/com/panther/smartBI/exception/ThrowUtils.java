package com.panther.smartBI.exception;

import com.panther.smartBI.common.ErrorCode;


/**
 * 抛异常工具类
 *
 */
public class ThrowUtils {
    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param runtimeException 运行异常
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 异常编号
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition 条件
     * @param errorCode 异常编号
     * @param message 异常信息
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
