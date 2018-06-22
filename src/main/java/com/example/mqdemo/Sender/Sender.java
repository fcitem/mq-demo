package com.example.mqdemo.Sender;

import com.example.mqdemo.config.DirectConfig;
import com.example.mqdemo.config.TopicConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fengchao
 * @date 2018-06-08
 */
@RestController
public class Sender {
    @Autowired
    RabbitTemplate rabbitTemplate;
    static AtomicInteger count=new AtomicInteger(0);
    @RequestMapping("test")
    public String test(){
        MessageProperties messageProperties=new MessageProperties();
//        messageProperties.setExpiration("5000");
        Message message =new Message(String.valueOf(count).getBytes(),messageProperties);
        message.getMessageProperties();
        count.addAndGet(1);
//        rabbitTemplate.send(TopicConfig.TOPICEXCHANGE,"as",message);
        rabbitTemplate.convertAndSend(TopicConfig.TOPICEXCHANGE,"as",message,new CorrelationData(String.valueOf(System.currentTimeMillis())));
        return "success";
    }

    @RequestMapping("direct")
    public String test2(){
        MessageProperties messageProperties=new MessageProperties();
//        messageProperties.setExpiration("5000");
        Message message =new Message(String.valueOf(count).getBytes(),messageProperties);
        message.getMessageProperties();
        count.addAndGet(1);
        rabbitTemplate.invoke(operations -> {
            operations.convertAndSend(DirectConfig.DIRECTEXCHANGE,"b",message,new CorrelationData(String.valueOf(count)));
            operations.waitForConfirms(10000);
            return false;
        });
        rabbitTemplate.convertAndSend(DirectConfig.DIRECTEXCHANGE,"b",message,new CorrelationData(String.valueOf(count)));
        return "success";
    }
}
