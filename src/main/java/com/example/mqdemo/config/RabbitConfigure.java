package com.example.mqdemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengchao
 * @date 2018-05-21
 */
@Configuration
public class RabbitConfigure {

    public static final Logger LOGGER=LoggerFactory.getLogger(RabbitConfigure.class);
    @Bean
    public RabbitTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
//        rabbitTemplate.setReplyTimeout(60000);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(confirmCallback(rabbitTemplate));
//        rabbitTemplate.waitForConfirms(60000);
//        rabbitTemplate.waitForConfirmsOrDie(6000);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                LOGGER.error("send to exchange ={} routingKey={},didn't match any queue.",exchange,routingKey);
            }
        });
        return rabbitTemplate;
    }
    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback(RabbitTemplate rabbitTemplate){
        return new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                if(!b){
                    LOGGER.error("消息发送失败到exchange失败,time={}",System.currentTimeMillis()-Long.valueOf(correlationData.getId()));
                }else {
//                    System.out.println("消息发送到exchange成功,correlationId="+correlationData.getId());
                }
            }
        };
    }
    @Bean("topicContainerFactory")
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory=new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(10);
        configurer.configure(simpleRabbitListenerContainerFactory,connectionFactory);
        return simpleRabbitListenerContainerFactory;
    }
    @Bean
    public MessageConverter messageConverter(){
        return new SimpleMessageConverter();
    }
}
