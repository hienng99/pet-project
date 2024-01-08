package com.nvhien.register_service.rabbitmq;

import com.nvhien.register_service.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public Result sendMessage(String message) {
        log.info("Send message to rabbitMQ: {}", message);
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            return Result.SUCCESS;
        } catch (AmqpException amqpException) {
            return Result.SEND_MSG_TO_RABBIT_FAILED;
        }

    }
}
