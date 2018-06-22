package com.example.mqdemo.listener;

import com.example.mqdemo.config.TopicConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author fengchao
 * @date 2018-06-07
 */
@Component
public class TopicHandler {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @RabbitListener(queues = TopicConfig.TOPICQUEUE,concurrency = "10")
    public void handler(Message message, Channel channel) throws IOException, InterruptedException {

        System.out.println("收到主题消息============="+new String(message.getBody()));
//        Thread.sleep(3000);
        System.out.println(Thread.currentThread().getName()+"="+new String(message.getBody())+"完成");
        /*GetResponse response=channel.basicGet(TopicConfig.TOPICQUEUE,false);
        response.getMessageCount();*/
        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        channel.basicCancel(message.getMessageProperties().getConsumerTag());
    }
}
