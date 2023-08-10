package com.panther.smartBI.mq;

import com.panther.smartBI.constant.BiMQConstant;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;


/**
 * @author Gin 琴酒
 * @data 2023/8/4 21:57
 */
public class Consumer {

    private final static String QUEUE_NAME = BiMQConstant.BI_QUEUE_NAME;

    public static void main(String[] argv) throws Exception {
        // 创建连接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("8.130.65.179");
        factory.setUsername("panther");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 创建队列
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        // 定义了如何处理消息,处理完成之后的回调，如果取消了自动应答还需要在这里应答
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println(" [x] Received '" + message + "'");
        };
        // 消费消息，会持续阻塞
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }


}
