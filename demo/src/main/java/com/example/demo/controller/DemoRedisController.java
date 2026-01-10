package com.example.demo.controller;

import com.example.demo.ResponseDTOs.RedisResponseDto;
import com.example.demo.mapper.RedisMapper;
import com.example.demo.service.DemoRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/redis")
public class DemoRedisController {

    private final DemoRedisService demoRedisService;
    private final RedisMapper redisMapper;

    @PostMapping("/saveAll")
    public String saveValue() {
        demoRedisService.save();
        return "Values saved in Redis!";
    }

    @PostMapping("/save")
    public String saveValue(Integer id) {
        demoRedisService.save(id.toString());
        return "Value saved in Redis!";
    }

    @GetMapping("/load")
    public RedisResponseDto loadValue() {
//        List<String> keyValues = demoRedisService.load();
        Set<Object> redisSet = demoRedisService.load();
        return RedisResponseDto.builder()
                .redisSet(redisSet)
                .build();
    }

    @GetMapping("/load/{key}")
    public Object loadValue(Integer key) {
        return demoRedisService.load(key);
    }

    @DeleteMapping("/delete")
    public void delete(Integer key){
        demoRedisService.delete(key.toString());
    }
}
