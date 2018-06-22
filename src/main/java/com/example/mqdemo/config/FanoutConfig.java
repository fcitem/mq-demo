package com.example.mqdemo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**广播exchange
 * @author fengchao
 * @date 2018-06-07
 */
@Configuration
public class FanoutConfig {
    public static final String FANOUTQUEUE="fanoutQueue";
    public static final String FANOUTEXCHANGE="fanoutExchange";

    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUTEXCHANGE,false,true);
    }
    @Bean
    Queue fanoutQueue(){
        return new Queue(FANOUTQUEUE);
    }
    @Bean
    Binding queueBinding(){
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }
}
