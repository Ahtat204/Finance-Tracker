package org.asue24.financetrackerbackend.Configuration;


import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.serialization.Mapper;
import io.github.bucket4j.redis.jedis.Bucket4jJedis;
import io.github.bucket4j.redis.jedis.cas.JedisBasedProxyManager;
import org.asue24.financetrackerbackend.entities.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.time.Duration;
import java.util.function.Supplier;

import static java.time.Duration.ofSeconds;

@Configuration
public class GlobalConfigs {
    @Value("${spring.data.redis.host}")
    String host;
    @Value("${spring.data.redis.port}")
    int port;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig().
                entryTtl(Duration.ofMinutes(10)).
                prefixCacheNameWith("transaction:")
                .disableCachingNullValues();
    }

    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.
                builder(connectionFactory)
                .cacheDefaults(redisCacheConfiguration())
                .build();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        var template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Transaction.class));
        return template;
    }
    @Bean
    public JedisBasedProxyManager jedisBasedProxyManager(JedisConnectionFactory jedisConnectionFactory) {
      /*  var poolField = JedisConnectionFactory.class.getDeclaredField("pool");
        poolField.setAccessible(true);*/
        var poolConfig = new JedisPoolConfig();
        var jedisPool=new JedisPool(poolConfig, host, port, Protocol.DEFAULT_TIMEOUT);
        return Bucket4jJedis.casBasedBuilder(jedisPool).expirationAfterWrite(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(ofSeconds(10)))
                .keyMapper(Mapper.STRING)
                .build();
    }
    @Bean
    public Supplier<BucketConfiguration> bucketConfiguration() {
        return ()->BucketConfiguration.builder().addLimit(Bandwidth.simple(10L,Duration.ofMinutes(2L))).build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES, true);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

}