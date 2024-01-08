package com.nvhien.notification.service;

import com.nvhien.notification.model.Mail;
import com.nvhien.notification.util.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public Result sendSimpleMail(Mail mail) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(mail.getRecipient());
            mailMessage.setSubject(mail.getSubject());
            mailMessage.setText(mail.getContent());

            javaMailSender.send(mailMessage);
            log.info("Send mail to {} successfully.", mail.getRecipient());
            return Result.SUCCESS;
        } catch (MailException mailException) {
            log.error("Send mail fail: {}", mailException.getMessage());
            return Result.SEND_MAIL_FAIL;
        }
    }
}