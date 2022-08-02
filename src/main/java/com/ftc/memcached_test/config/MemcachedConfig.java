package com.ftc.memcached_test.config;

import lombok.RequiredArgsConstructor;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author: 冯铁城 [17615007230@163.com]
 * @date: 2022-08-02 14:31:32
 * @describe: memcached客户端
 */
@Configuration
@RequiredArgsConstructor
public class MemcachedConfig {

    private final MemcachedProperties memcachedProperties;

    @Bean
    public MemcachedClient getMemcachedClient() throws IOException {

        //1.封装配置
        MemcachedClientBuilder memcachedClientBuilder = new XMemcachedClientBuilder(memcachedProperties.getAddress());
        memcachedClientBuilder.setConnectionPoolSize(memcachedProperties.getPoolSize());
        memcachedClientBuilder.setOpTimeout(memcachedProperties.getOpTimeout());

        //2.返回
        return memcachedClientBuilder.build();
    }
}
