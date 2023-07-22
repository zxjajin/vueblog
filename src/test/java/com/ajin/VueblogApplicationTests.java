package com.ajin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

import javax.sql.DataSource;
import java.util.List;

@Slf4j
@SpringBootTest
class VueblogApplicationTests {
    @Autowired
    DataSource dataSource;
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
    @Test
    void test1(){
//        System.out.println(dataSource.getClass());
        int[] ints = new int[]{76,82,83,84,84,84,85,85,85,87,89,90,91,92,93};
        int sum =0;
        for(int i = 0;i<ints.length;i++){

            sum += ints[i];
        }
        int average = sum/ints.length;
        System.out.println("总分："+sum+"平均分:"+average+" "+ints.length+"科");

    }


}
