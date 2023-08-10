package com.panther.smartBI.controller;

import com.panther.smartBI.common.BaseResponse;
import com.panther.smartBI.common.ResultUtils;
import com.panther.smartBI.constant.BiConstant;
import com.panther.smartBI.manager.AiManager;
import com.panther.smartBI.model.dto.ai.ChatRequest;
import com.panther.smartBI.model.dto.ai.StoryRequest;
import com.panther.smartBI.model.vo.StoryResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Gin 琴酒
 * @data 2023/8/7 10:17
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AiManager aiManager;

    /**
     * AI 聊天
     * @return
     */
    @PostMapping("/chat")
    public BaseResponse<String> genChat(ChatRequest chatRequest){
        String answer = aiManager.doChat(BiConstant.CHAT_MODEL_ID, chatRequest.getMessage());
        return ResultUtils.success(answer);
    }

    /**
     * AI 生成故事
     * @return
     */
    @PostMapping("/genStory")
    public BaseResponse<StoryResponse> genStory(StoryRequest storyRequest){

        StoryResponse storyResponse = new StoryResponse();
        storyResponse.setTitle(storyRequest.getTitle());
        String content = aiManager.doChat(BiConstant.STORY_MODEL_ID, storyRequest.getTitle());
        storyResponse.setContent(content);

        return ResultUtils.success(storyResponse);
    }

    /**
     * AI 菩萨
     * @return
     */
    @PostMapping("/genPurdue")
    public BaseResponse<String> genPurdue(String worried){

        String content = aiManager.doChatByClient(BiConstant.PUSA_MODEl_ID, worried);

        return ResultUtils.success(content);
    }

}
