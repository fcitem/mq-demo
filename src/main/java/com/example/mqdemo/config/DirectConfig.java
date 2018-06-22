package com.example.mqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengchao
 * @date 2018-06-12
 */
@Configuration
public class DirectConfig {

    public static final String DIRECTQUEUE="fc.directQueue";
    public static final String DIRECTEXCHANGE="fc.directExchange";

    @Bean
    public DirectExchange direct2Exchange(){
        return new DirectExchange(DIRECTEXCHANGE);
    }
    @Bean
    public Queue queue2(){
//        Map<String,Object> map=new HashMap<>();
        /*map.put("x-max-length",1000);*/
        /*map.put("x-overflow","reject-publish");*/
        return new Queue(DIRECTQUEUE);
    }
    @Bean
    public Binding queueBinding2(){
        return BindingBuilder.bind(queue2()).to(direct2Exchange()).with("a");
    }
}
