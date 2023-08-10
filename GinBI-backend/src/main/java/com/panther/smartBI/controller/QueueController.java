package com.panther.smartBI.controller;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Gin 琴酒
 * @data 2023/8/2 17:28
 */
@RestController
@Slf4j
@RequestMapping("/queue")
@Profile({"dev","local"})
public class QueueController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 测试添加任务
     * @param name 线程名称
     * @return 信息
     */
    @GetMapping("/add")
    public void addTask(String name){
        CompletableFuture.runAsync(()->{
            log.info("{} : 线程在润 {}",Thread.currentThread().getName(),name);
            try {
                Thread.sleep(60000 * 10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        },threadPoolExecutor);
    }

    @GetMapping("/get")
    public String QueryTask(){
        Map<String, Object> map = new HashMap<>();
        int size = threadPoolExecutor.getQueue().size();
        map.put("队列长度",size);
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        map.put("已完成任务数",completedTaskCount);
        long taskCount = threadPoolExecutor.getTaskCount();
        map.put("任务总数",taskCount);
        int activeCount = threadPoolExecutor.getActiveCount();
        map.put("存活的线程数",activeCount);
        return JSONUtil.toJsonStr(map);
    }

}
