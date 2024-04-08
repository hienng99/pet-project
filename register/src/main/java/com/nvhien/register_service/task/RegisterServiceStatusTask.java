package com.nvhien.register_service.task;

import com.nvhien.register_service.util.RegisterConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
@Order(0)
@Slf4j
public class RegisterServiceStatusTask implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.set(RegisterConst.SERVICE_NAME, Integer.toString(1));
                log.warn("Set service status to 1.");
            }
        }
    }
}
