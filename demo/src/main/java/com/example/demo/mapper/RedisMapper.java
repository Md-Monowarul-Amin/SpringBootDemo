package com.example.demo.mapper;

import com.example.demo.ResponseDTOs.RedisResponseDto;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface RedisMapper {
    RedisResponseDto mapToResponseDto(List<String> redisKeys, Set<Object> redisSet);
}
