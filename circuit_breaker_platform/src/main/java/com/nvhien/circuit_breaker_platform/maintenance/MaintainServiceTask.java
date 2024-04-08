package com.nvhien.circuit_breaker_platform.maintenance;

import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class MaintainServiceTask {
    public void execute() {
        try (JedisPool pool = new JedisPool("localhost", 6379)) {
            try (Jedis jedis = pool.getResource()) {
                jedis.set("register_service", Integer.toString(0));
            }
        }
    }
}
