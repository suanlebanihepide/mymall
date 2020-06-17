package com.example.mymmal.Service;

//封装本地缓存操作类
public interface CacheService {
    //存取方法

    void  setCommonCache(String key,Object value);

    Object getFromCommonCache(String key);

}
