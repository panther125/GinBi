package com.panther.smartBI.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gin 琴酒
 * @data 2023/8/8 16:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevChatRequest {
    private Long modelId;
    private String message;
    private static final long serialVersionUID = 1L;
}
