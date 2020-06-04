package com.codeagles.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: Codeagles
 * Date: 2020/6/5
 * Time: 6:48 上午
 * <p>
 * Description:
 */
@Component
public class RedisOperator {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value, long timeout){
        stringRedisTemplate.opsForValue().set(key,value,timeout);
    }

    public void set(String key, String value ){
        stringRedisTemplate.opsForValue().set(key,value);
    }
    
    public String get(String key){
        return (String)stringRedisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

}
