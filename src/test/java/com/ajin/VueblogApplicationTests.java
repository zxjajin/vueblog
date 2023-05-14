package com.ajin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;
@Slf4j
@SpringBootTest
class VueblogApplicationTests {
//    @Autowired
//    DataSource dataSource;
    @Test
    void contextLoads() {
        Jedis jedis = new Jedis("8.138.58.49",6379);
        jedis.auth("123456");
        System.out.println(jedis.ping());
        log.info("redis conn status:{}","连接成功");
        log.info("redis ping retvalue:{}",jedis.ping());

        jedis.set("k1","jedis");
        log.info("k1 value:{}",jedis.get("k1"));
    }

}
