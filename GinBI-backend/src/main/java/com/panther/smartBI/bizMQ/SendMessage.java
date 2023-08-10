package com.panther.smartBI.bizMQ;

import com.panther.smartBI.constant.BiMQConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Gin 琴酒
 * @data 2023/8/5 23:37
 */
@Component
public class SendMessage {

    @Resource
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(BiMQConstant.BI_EXCHANGE_NAME, BiMQConstant.BI_ROUTING_KEY, message);
    }

}
