package com.ftc.memcached_test.config;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeoutException;

@SpringBootTest
class MemcachedConfigTest {

    /**
     * key
     */
    private static final String KEY = "key";

    /**
     * value
     */
    private static final String VALUE = "value";

    @Autowired
    public MemcachedClient memcachedClient;

    @BeforeEach
    void setUp() throws InterruptedException, TimeoutException, MemcachedException {
        memcachedClient.delete(KEY);
    }

    @Test
    void testSaveAndGet() throws InterruptedException, TimeoutException, MemcachedException {

        //1.add命令
        boolean addSuccess = memcachedClient.add(KEY, 0, VALUE);
        Assert.isTrue(addSuccess);

        //2.再次添加
        addSuccess = memcachedClient.add(KEY, 0, VALUE);
        Assert.isFalse(addSuccess);

        //3.set命令
        boolean setSuccess = memcachedClient.set(KEY, 0, VALUE);
        Assert.isTrue(setSuccess);

        //4.replace命令
        boolean replaceSuccess = memcachedClient.replace(KEY, 0, VALUE + "replace");
        Assert.isTrue(replaceSuccess);

        //5.append命令
        boolean appendSuccess = memcachedClient.append(KEY, "append");
        Assert.isTrue(appendSuccess);

        //6.prepend命令
        boolean prependSuccess = memcachedClient.prepend(KEY, "prepend");
        Assert.isTrue(prependSuccess);

        //7.查询
        Object o = memcachedClient.get(KEY);
        Assert.isTrue(ObjectUtil.isNotNull(o));
        Assert.isTrue("prependvaluereplaceappend".equals(o.toString()));
    }

    @Test
    void testCasAndGets() throws InterruptedException, TimeoutException, MemcachedException {

        //1.未保存key,cas修改
        boolean casSuccess = memcachedClient.cas(KEY, 0, VALUE, 1);
        Assert.isFalse(casSuccess);

        //2.保存键值
        boolean addSuccess = memcachedClient.add(KEY, 0, VALUE);
        Assert.isTrue(addSuccess);

        //3.未获取token,再次cas修改
        casSuccess = memcachedClient.cas(KEY, 0, VALUE, 1);
        Assert.isFalse(casSuccess);

        //4.gets获取token
        GetsResponse<Object> gets = memcachedClient.gets(KEY);
        long cas = gets.getCas();

        //5.再次cas
        casSuccess = memcachedClient.cas(KEY, 0, VALUE + "cas", cas);
        Assert.isTrue(casSuccess);

        //6.查询
        Object o = memcachedClient.get(KEY);
        Assert.isTrue(ObjectUtil.isNotNull(o));
        Assert.isTrue("valuecas".equals(o.toString()));
    }

    @Test
    void testDeleteAndIncrAndDecrAndFlushAll() throws InterruptedException, TimeoutException, MemcachedException {

        //1.保存数据
        boolean setSuccess = memcachedClient.set(KEY, 0, VALUE);
        Assert.isTrue(setSuccess);

        //2.删除
        boolean deleteSuccess = memcachedClient.delete(KEY);
        Assert.isTrue(deleteSuccess);

        //3.查询校验
        Object o = memcachedClient.get(KEY);
        Assert.isNull(o);

        //4.存储数字键值对
        boolean addSuccess = memcachedClient.add(KEY, 0, "10");
        Assert.isTrue(addSuccess);

        //5.incr
        long incr = memcachedClient.incr(KEY, 10);
        Assert.isTrue(20 == incr);

        //6.decr
        long decr = memcachedClient.decr(KEY, 15);
        Assert.isTrue(5 == decr);

        //7.flushAll
        memcachedClient.flushAll();

        //8.查询
        o = memcachedClient.get(KEY);
        Assert.isNull(o);
    }
}