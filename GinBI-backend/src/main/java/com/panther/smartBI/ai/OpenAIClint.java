package com.panther.smartBI.ai;

/**
 * @author Gin 琴酒
 * @data 2023/8/6 22:55
 */

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class OpenAIClint {
    public static void main(String[] args) {
        String url = "https://api.openai.com/v1/chat/completions";
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> newMap = new HashMap<>();
        newMap.put("role","user");
        newMap.put("content","你是谁");
        String messages = JSONUtil.toJsonStr(newMap);
        map.put("model","gpt-3.5-turbo");
        map.put("messages",messages);

        String result = JSONUtil.toJsonStr(map);

        String message = HttpRequest.post(url)
                .header("Authorization", "填自己的")
                .header("Content-Type","application/json")
                .body(result)
                .execute()
                .body();
        System.out.println(message);
    }
}
