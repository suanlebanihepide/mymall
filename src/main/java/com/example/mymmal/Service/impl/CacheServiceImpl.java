/*
 * @Author: shenzheng
 * @Date: 2020/6/16 3:54
 */

package com.example.mymmal.Service.impl;

import com.example.mymmal.Service.CacheService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {

    private Cache<String, Object> commonCache = null;

    @PostConstruct
    public void init() {

        commonCache = CacheBuilder.newBuilder()
                .initialCapacity(10) //设置缓存初始容量
                .maximumSize(100) //设置缓存最大可以存储100个Key
                .expireAfterWrite(60, TimeUnit.SECONDS).build();    //设置写缓存后多久过期

    }


    @Override
    public void setCommonCache(String key, Object value) {
        commonCache.put(key, value);
    }

    @Override
    public Object getFromCommonCache(String key) {
        return commonCache.getIfPresent(key);
    }
}