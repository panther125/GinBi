package com.panther.smartBI.bizMQ;

import com.panther.smartBI.common.ErrorCode;
import com.panther.smartBI.constant.BiConstant;
import com.panther.smartBI.constant.BiMQConstant;
import com.panther.smartBI.exception.BusinessException;
import com.panther.smartBI.exception.ThrowUtils;
import com.panther.smartBI.manager.AiManager;
import com.panther.smartBI.model.entity.Chart;
import com.panther.smartBI.model.enums.ChartStatusEnum;
import com.panther.smartBI.service.ChartService;
import com.panther.smartBI.utils.UserInputUtils;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费者
 *
 * @author Gin 琴酒
 * @data 2023/8/5 23:39
 */
@Slf4j
@Component
public class ReceiveMessage {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    /**
     *  RabbitListener 这个注解会自动填充下面参数
     * @param message 消息
     * @param channel 信道
     * @param deliveryTag 确认消息的标签
     */
    @SneakyThrows // lombok 提供的消除异常注解。原理：利用泛型将我们传入的Throwable强转为RuntimeException
    @RabbitListener(queues = {BiMQConstant.BI_QUEUE_NAME}, ackMode = "MANUAL") // ackMode人工确认机制，
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        // 消息为空
        if(StringUtils.isBlank(message)){
            // 消息的标识  取消批量确认  不放回队列
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"消息为空！");
        }
        // 解析消息队列中的消息（存char的id）
        long chartId = Long.parseLong(message);

        // 根据chart 构建用户输入
        String userInput = UserInputUtils.BuilderUserInput(chartId,chartService);

        Chart update = new Chart();
        update.setId(chartId);
        // 更新状态为执行中
        update.setStatus(ChartStatusEnum.CHART_STATUS_RUNNING.getValue());
        update.setExecMessage(ChartStatusEnum.CHART_STATUS_RUNNING.getText());
        if (!chartService.updateById(update)) {
            // 如果执行失败 更新状态
            update.setStatus(ChartStatusEnum.CHART_STATUS_FAILURE.getValue());
            update.setExecMessage(ChartStatusEnum.CHART_STATUS_FAILURE.getText());
            chartService.updateById(update);
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"图标生成失败！");
        }
        String aiRes = aiManager.doChat(BiConstant.BI_MODEL_ID_S, userInput);
        //截取AI数据
        final String str = "=>=>=>";
        String[] aiData = aiRes.split(str);
        String genChart = aiData[1].trim();
        String genResult = aiData[2].trim();
        //log.info("aiData len = {} data = {}", aiData.length, aiRes);
        if(StringUtils.isBlank(genChart) || StringUtils.isBlank(genResult)){
            update.setStatus(ChartStatusEnum.CHART_STATUS_FAILURE.getValue());
            update.setExecMessage(ChartStatusEnum.CHART_STATUS_FAILURE.getText());
            chartService.updateById(update);
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"AI 分析失败!");
        }
        // 更新状态为成功
        update.setStatus(ChartStatusEnum.CHART_STATUS_SUCCESS.getValue());
        update.setExecMessage(ChartStatusEnum.CHART_STATUS_SUCCESS.getText());
        update.setGenChart(genChart);
        update.setGenResult(genResult);
        chartService.updateById(update);
        channel.basicAck(deliveryTag,false);
    }

}
