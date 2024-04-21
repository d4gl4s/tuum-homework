package com.tuum.tuumhomework.configuration;

import com.tuum.tuumhomework.enums.RoutingKey;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    RoutingKey routingKey;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    public Queue myQueue() {
        return new Queue("queue");
    }

    @Bean
    public Binding binding(Queue myQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(myQueue).to(directExchange).with(routingKey.getKey());
    }
}

