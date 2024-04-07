package com.nvhien.register_service.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.register.queue.name}")
    private String registerQueue;

    @Value("${rabbitmq.circuit-breaker.queue.name}")
    private String circuitBreakerQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.register.routing.key}")
    private String registerRoutingKey;

    @Value("${rabbitmq.circuit-breaker.routing.key}")
    private String circuitBreakerRoutingKey;

    @Bean
    public Queue registerQueue(){
        return new Queue(registerQueue);
    }

    @Bean
    public Queue circuitBreakerQueue(){
        return new Queue(circuitBreakerQueue);
    }

    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding registerBinding(){
        return BindingBuilder
                .bind(registerQueue())
                .to(exchange())
                .with(registerRoutingKey);
    }

    @Bean
    public Binding circuitBreakerBinding(){
        return BindingBuilder
                .bind(circuitBreakerQueue())
                .to(exchange())
                .with(circuitBreakerRoutingKey);
    }
}
