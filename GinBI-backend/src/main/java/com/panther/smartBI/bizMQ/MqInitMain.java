package com.panther.smartBI.bizMQ;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 初始化一个交换机和队列
 *
 * @author Gin 琴酒
 * @data 2023/8/5 23:53
 */
public class MqInitMain {

    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("");
            factory.setUsername("panther");
            factory.setPassword("123456");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "code_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = "code_queue";
            /**
             * 持久化
             * 共享队列
             * 自动删除
             * 其他参数
             */
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingKey");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

}
