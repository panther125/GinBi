package com.panther.smartBI.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gin 琴酒
 * @data 2023/8/9 10:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZelinAIRequest {

    /**
     * 请求体示例
     * {
     *     "app_id": "xxx",
     *     "request_id": "xxx",
     *     "uid": "xxx",
     *     "content": "你好"
     * }
     */
    private String app_id;
    private String request_id;
    private String uid;
    private String content;
    private static final long serialVersionUID = 1L;

}
