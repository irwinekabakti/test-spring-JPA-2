package com.example.test_spring_JPA_2.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BlacklistAuthRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;

//    @Autowired
    public BlacklistAuthRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void blacklistToken(String token) {
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted");
    }

    public boolean isTokenBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
}
