package org.asue24.financetrackerbackend.services.caching;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class RedisService<T> implements CachingService<T> {

    private final RedisTemplate<String, T> redisTemplate;
    @Autowired
    public RedisService(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    /**
     * @param key
     * @param value
     */
    @Override
    public void put(String key, T value) {
        redisTemplate.opsForValue().set(key, value, 10,TimeUnit.MINUTES);
    }
    /**
     * @param key
     * @return
     */
    @Override
    public T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    /**
     * @param key
     */
    @Override
    public void Evict(String key) {
        redisTemplate.delete(key);
    }
}
