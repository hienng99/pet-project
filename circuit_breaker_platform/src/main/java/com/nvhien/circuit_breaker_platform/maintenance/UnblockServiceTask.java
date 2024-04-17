package com.nvhien.circuit_breaker_platform.maintenance;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class UnblockServiceTask {
    public void execute() {
        try (JedisPool pool = new JedisPool("172.23.224.165", 6379)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.set("register_service", Integer.toString(1));
            }
        }
    }
}
