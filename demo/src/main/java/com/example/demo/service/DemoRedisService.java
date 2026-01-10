package com.example.demo.service;

import com.example.demo.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DemoRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private List<String> generateRedisKeys() {
        List<String> keyList = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            keyList.add(Integer.toString(i));
        }
        return keyList;
    }

    private String generateValue(String  key) {
        return key + "-value";
    }

    private void generateRedisSet(List<String> redisKeys) {
        redisKeys.forEach(key -> redisTemplate.opsForSet().add(Constants.REDIS_ALL_KEYS_SET, generateValue(key)));
    }

    public void save(String key) {
        redisTemplate.opsForValue().set(key, generateValue(key), Duration.ofMinutes(2));
        redisTemplate.opsForSet().add(Constants.REDIS_ALL_KEYS_SET, generateValue(key));
    }

    public void save() {
        List<String> redisKeys = generateRedisKeys();
        redisKeys.forEach(key -> {
            redisTemplate.opsForValue().set(key, generateValue(key), Duration.ofMinutes(2));
        });
        generateRedisSet(redisKeys);
    }

    public Set<Object> load() {
        return redisTemplate.opsForSet().members(Constants.REDIS_ALL_KEYS_SET);
    }

    public Object load(int key) {
        return redisTemplate.opsForValue().get(Integer.toString(key));
    }


    public void delete(String key) {
        redisTemplate.delete(key);
        redisTemplate.opsForSet().remove(Constants.REDIS_ALL_KEYS_SET, generateValue(key));
    }

    public void deleteFromSet(String key) {
        redisTemplate.opsForSet().remove(Constants.REDIS_ALL_KEYS_SET, generateValue(key));
    }

}
