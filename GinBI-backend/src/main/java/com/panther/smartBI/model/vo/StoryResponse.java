package com.panther.smartBI.model.vo;

import lombok.Data;

/**
 * @author Gin 琴酒
 * @data 2023/8/7 10:21
 */
@Data
public class StoryResponse {

    /**
     * 用户输入的主题
     */
    private String title;

    /**
     * 生成的故事
     */
    private String content;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;
}
