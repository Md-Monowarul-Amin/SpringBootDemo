package com.example.springboot.mapper;

import com.example.springboot.dto.*;
import com.example.springboot.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequestDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .build();
    }

    public void updateEntityFromDTO(UserUpdateRequestDTO dto, User existingUser) {
        if (dto.getUsername() != null) existingUser.setUsername(dto.getUsername());
        if (dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
