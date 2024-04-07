package com.nvhien.notification.service;

import com.nvhien.notification.model.Mail;
import com.nvhien.notification.util.Result;

public interface IEmailService {
    Result sendSimpleMail(Mail details);
}
