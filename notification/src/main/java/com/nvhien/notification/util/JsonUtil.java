package com.nvhien.notification.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtil {
    public <T> T stringToObject(String jsonString, Class<T> cl) {
        try {
            return new ObjectMapper().readValue(jsonString, cl);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
