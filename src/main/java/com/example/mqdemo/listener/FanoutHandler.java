package com.example.mqdemo.listener;

import com.example.mqdemo.config.FanoutConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author fengchao
 * @date 2018-06-07
 */
@Component
public class FanoutHandler {

    @RabbitListener(queues = FanoutConfig.FANOUTQUEUE,concurrency = "10")
    public void handler(Message message, Channel channel) throws IOException {

        System.out.println("收到广播消息============="+new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
