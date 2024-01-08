package com.nvhien.notification.service;

import com.nvhien.notification.model.Mail;
import com.nvhien.notification.model.User;
import com.nvhien.notification.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumer {
    private final EmailService emailService;
    private final String SUBJECT = "WELCOME";
    private final String CONTENT_FORMAT = "Hello %s, register successfully!";

    @RabbitListener(queues = {"${rabbitmq.queue.name}"})
    public void consume(String message) {
        log.info("Received message from RabbitMQ: {}", message);
        Mail mail = new Mail();
        var user = JsonUtil.stringToObject(message, User.class);
        mail.setSubject(SUBJECT);
        mail.setRecipient(user.getMailAddress());
        String content = String.format(CONTENT_FORMAT, user.getFullName());
        mail.setContent(content);
        log.info(emailService.sendSimpleMail(mail).getDescription());
    }
}