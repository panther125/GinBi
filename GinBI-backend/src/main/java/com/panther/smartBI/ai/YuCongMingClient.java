package com.panther.smartBI.ai;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.panther.smartBI.common.BaseResponse;
import com.panther.smartBI.constant.BiConstant;
import com.panther.smartBI.model.chat.DevChatRequest;
import com.panther.smartBI.model.chat.DevChatResponse;
import com.panther.smartBI.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 调用鱼聪明 AI 的客户端
 *
 */
@Service
@Slf4j
public class YuCongMingClient {


    private static final String HOST = "https://www.yucongming.com/api/dev";
    //private static final String HOST = "124.223.215.170:443/api/dev";

    private final String accessKey = "自己有";

    private final String secretKey = "自己有";

    /**
     * 对话
     *
     * @param devChatRequest
     * @return
     */
    public BaseResponse<DevChatResponse> doChat(DevChatRequest devChatRequest) {
        String url = HOST + "/chat";
        String json = JSONUtil.toJsonStr(devChatRequest);
        String result = HttpRequest.post(url)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute()
                .body();
        TypeReference<BaseResponse<DevChatResponse>> typeRef = new TypeReference<BaseResponse<DevChatResponse>>() {
        };
        return JSONUtil.toBean(result, typeRef, false);
    }
    /**
     * 获取请求头
     *
     * @param body 请求参数
     * @return
     */
    private Map<String, String> getHeaderMap(String body) {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey", accessKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        String encodedBody = SecureUtil.md5(body);
        hashMap.put("body", encodedBody);
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.genSign(encodedBody, secretKey));
        return hashMap;
    }

    public static void main(String[] args) {
        YuCongMingClient yuCongMingClient = new YuCongMingClient();
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(BiConstant.PUSA_MODEl_ID);
        devChatRequest.setMessage("我心乱了");
        BaseResponse<DevChatResponse> devChatResponseBaseResponse = yuCongMingClient.doChat(devChatRequest);
        System.out.println(devChatResponseBaseResponse);
        System.out.println("==================================");
        DevChatResponse data = devChatResponseBaseResponse.getData();
        if (data != null) {
            String content = data.getContent();
            System.out.println(content);
        }
    }
}
