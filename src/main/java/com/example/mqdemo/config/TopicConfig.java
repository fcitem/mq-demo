package com.example.mqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**广播exchange
 * @author fengchao
 * @date 2018-06-07
 */
@Configuration
public class TopicConfig {
    public static final String TOPICQUEUE="topicQueue";
    public static final String TOPICQUEUEA="topicQueuea";
    public static final String TOPICEXCHANGE="topicExchange";

    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange(TOPICEXCHANGE);
    }
    @Bean
    Queue queue(){
//        Map<String,Object> map=new HashMap<>();
        /*map.put("x-max-length",1000);*/
        /*map.put("x-overflow","reject-publish");*/
        return new Queue(TOPICQUEUE,true,false,true);
    }
    @Bean
    Queue Queuea(){
        return new Queue(TOPICQUEUEA);
    }
    @Bean
    Binding queueBinding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("#");
    }
   /* @Bean
    Binding bindinga(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with("a");
    }*/
}
