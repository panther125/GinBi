package com.panther.smartBI.ai;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.panther.smartBI.common.BaseResponse;
import com.panther.smartBI.model.chat.DevChatResponse;
import com.panther.smartBI.model.chat.ZelinAIRequest;

import java.util.HashMap;
import java.util.Map;

import static com.panther.smartBI.utils.ApiAuthUtils.generateNonce;

/**
 * @author Gin 琴酒
 * @data 2023/8/9 10:51
 */
public class ZelinaiClient {
    private static final String HOST = "https://zelinai.com";
    private static final String SYNCHRONIZATION = "/biz/v1/app/chat/sync";

    private final String ak = "自己有";

    private final String sk = "自己有";


    /**
     * 对话
     *{
     *     "app_id": "xxx",
     *     "request_id": "xxx",
     *     "uid": "xxx",
     *     "content": "你好"
     * }
     * @param zelinaiRequest
     * @return
     */
    public BaseResponse<DevChatResponse> doChat(ZelinAIRequest zelinaiRequest) {
        String url = HOST + SYNCHRONIZATION;
        String json = JSONUtil.toJsonStr(zelinaiRequest);
        String result = HttpRequest.post(url)
                .addHeaders(getHeaderMap(zelinaiRequest))
                .body(json)
                .execute()
                .body();
        TypeReference<BaseResponse<DevChatResponse>> typeRef = new TypeReference<BaseResponse<DevChatResponse>>() {};
        return JSONUtil.toBean(result, typeRef, false);
    }

    /**
     * 获取请求头
     *
     * @param zelinaiRequest 请求参数
     * @return
     */
    private Map<String, String> getHeaderMap(ZelinAIRequest zelinaiRequest) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("appkey", ak);
        String nonce= RandomUtil.randomNumbers(16);
        hashMap.put("nonce", nonce);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        hashMap.put("timestamp", timestamp);

        String app_id = zelinaiRequest.getApp_id();
        String request_id = zelinaiRequest.getRequest_id();
        String uid = zelinaiRequest.getUid();
        String content = zelinaiRequest.getContent();
        hashMap.put("app_id",app_id);
        hashMap.put("request_id",request_id);
        hashMap.put("uid",uid);
        hashMap.put("content",content);
        hashMap.put("signature", generateNonce(hashMap, sk));
//        hashMap.put("Content-Type", "application/json");
        hashMap.put("Content-Type", "application/json;charset=UTF-8");
        return hashMap;
    }

    public static void main(String[] args) {

        // 创建 ZelinaiClient 实例
        ZelinaiClient zelinaiClient = new ZelinaiClient();

        // 创建 DevChatRequest 对象并设置参数
        ZelinAIRequest devChatRequest = new ZelinAIRequest();
        devChatRequest.setApp_id("dWzAtnoAq8axkZZVSNsq3a");
        devChatRequest.setRequest_id("会话id标示会话");
        devChatRequest.setUid("146048");
        devChatRequest.setContent("生成一段凄惨的爱情故事");
        System.out.println(zelinaiClient.doChat(devChatRequest));
        // 调用 doChat 方法发送对话请求
        BaseResponse<DevChatResponse> response = zelinaiClient.doChat(devChatRequest);
    }
}
