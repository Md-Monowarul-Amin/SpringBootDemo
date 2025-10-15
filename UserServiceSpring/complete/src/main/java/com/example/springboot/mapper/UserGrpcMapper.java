package com.example.springboot.mapper;

import com.example.springboot.dto.*;
import com.example.springboot.grpc.*;
import org.springframework.stereotype.Component;

@Component
public class UserGrpcMapper {

    public UserCreateRequestDTO toCreateRequestDTO(CreateUserRequest request) {
        return UserCreateRequestDTO.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .build();
    }

    public UserUpdateRequestDTO toUpdateRequestDTO(UpdateUserRequest request) {
        return UserUpdateRequestDTO.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .build();
    }

    public UserResponse toGrpcResponse(UserDTO userDTO) {
        return UserResponse.newBuilder()
                .setId(userDTO.getId())
                .setUsername(userDTO.getUsername())
                .setEmail(userDTO.getEmail())
                .build();
    }
}
