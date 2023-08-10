package com.panther.smartBI.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Gin 琴酒
 * @data 2023/8/1 17:18
 */
@Configuration
@ConfigurationProperties(value = "spring.redis")
public class RedissonConfig {

    private int database;

    private String host;

    private String port;

    private String password;

    private int timeout;

    public void setDatabase(int database) {
        this.database = database;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setPassword(String password) {this.password = password;}

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(database)
                .setAddress("redis://"+host+":"+port)
                .setPassword(password)
                .setTimeout(timeout);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
