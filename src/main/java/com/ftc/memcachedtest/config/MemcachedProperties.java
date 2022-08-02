package com.ftc.memcachedtest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: 冯铁城 [17615007230@163.com]
 * @date: 2022-08-02 14:28:05
 * @describe: memcached配置
 */
@Data
@Component
@ConfigurationProperties("memcached")
public class MemcachedProperties {

    /**
     * 地址
     */
    private String address;

    /**
     * 链接池数量
     */
    private Integer poolSize;

    /**
     * 操作超时时间
     */
    private Integer opTimeout;
}
