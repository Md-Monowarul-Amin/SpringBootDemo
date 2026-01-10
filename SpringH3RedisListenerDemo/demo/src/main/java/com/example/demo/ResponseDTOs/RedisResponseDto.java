package com.example.demo.ResponseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RedisResponseDto {
//    private List<String> redisKeys;
    private Set<Object> redisSet;
}
