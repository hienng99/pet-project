package com.nvhien.notification.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Result {
    SUCCESS(0, "Success."),
    SEND_MAIL_FAIL(1, "Send mail failed.")
    ;

    private final int code;
    private final String description;
}
