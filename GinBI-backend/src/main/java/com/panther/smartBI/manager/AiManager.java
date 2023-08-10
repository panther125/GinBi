package com.panther.smartBI.manager;

import com.panther.smartBI.ai.YuCongMingClient;
import com.panther.smartBI.common.BaseResponse;
import com.panther.smartBI.common.ErrorCode;
import com.panther.smartBI.exception.BusinessException;
import com.panther.smartBI.model.chat.DevChatRequest;
import com.panther.smartBI.model.chat.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Gin 琴酒
 * @data 2023/7/30 16:48
 */
@Service
public class AiManager {

    @Resource
    private YuCongMingClient yuCongMingClient;

    /**
     * HttpUtils
     * @param modelId
     * @param message
     * @return
     */
    public String doChat(long modelId, String message){
        DevChatRequest devChatRequest = new DevChatRequest();

        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);

        BaseResponse<DevChatResponse> devChatResponseBaseResponse = yuCongMingClient.doChat(devChatRequest);
        if (devChatResponseBaseResponse == null || devChatResponseBaseResponse.getData() == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 响应异常");
        }
        return devChatResponseBaseResponse.getData().getContent();
    }

    /**
     * httpClient
     * @param modelId
     * @param message
     * @return
     */
    public String doChatByClient(long modelId, String message){
        DevChatRequest devChatRequest = new DevChatRequest();

        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);

        BaseResponse<DevChatResponse> devChatResponseBaseResponse = yuCongMingClient.doChat(devChatRequest);
        if (devChatResponseBaseResponse == null || devChatResponseBaseResponse.getData() == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 响应异常");
        }
        return devChatResponseBaseResponse.getData().getContent();
    }

}
