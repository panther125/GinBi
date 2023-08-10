package com.panther.smartBI.job.cycle;

import com.panther.smartBI.bizMQ.SendMessage;
import com.panther.smartBI.service.ChartService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时间将运行正在运行或者失败状态的图表重新放入消息队列中
 *
 * @author Gin 琴酒
 * @data 2023/8/6 14:49
 */
@Component
public class RestoreChart {

    @Resource
    private ChartService chartService;

    @Resource
    private SendMessage sendMessage;

    @Scheduled(cron = "0 */1 * * * ?")
    public void run() {
        List<Long> failedChart = chartService.getFailedChart();
        if(failedChart.size() > 0){
            // 将失败的图标放回队列
            failedChart.forEach(item ->{
                sendMessage.sendMessage(String.valueOf(item));
            });
        }
    }
}
