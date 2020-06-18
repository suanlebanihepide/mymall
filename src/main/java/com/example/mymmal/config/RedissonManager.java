/*
 * @Author: shenzheng
 * @Date: 2020/6/17 23:23
 */

package com.example.mymmal.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;


@Component
public class RedissonManager {

//    @Bean
//    public RedissonClient redisson() throws IOException {
//        // 本例子使用的是yaml格式的配置文件，读取使用Config.fromYAML，如果是Json文件，则使用Config.fromJSON
//        Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-config.yml"));
//        return Redisson.create(config);
//    }

    //    @Value("${spring.redis.host}")
    private String host = "39.106.127.76";

    //    @Value("${spring.redis.port}")
    private String port = "6379";

    //    @Value("${spring.redis.password}")
    private String password = "13892009451";
    private Redisson redisson = null;

    @PostConstruct
    private void init() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://39.106.127.76:6379");
        singleServerConfig.setPassword("13892009451");
        singleServerConfig.setDatabase(0);
        redisson = (Redisson) Redisson.create(config);
    }

    //
//
    public RedissonClient getRedisson() {
        return redisson;
    }


}
