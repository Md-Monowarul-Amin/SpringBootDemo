package com.example.springboot.service;

import com.example.springboot.dto.UserCreateRequestDTO;
import com.example.springboot.dto.UserDTO;
import com.example.springboot.dto.UserUpdateRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserCreateRequestDTO requestDTO);

    UserDTO getUserById(Long id);

    // UserDTO getUserByUsername(String username);

    UserDTO updateUser(Long id, UserUpdateRequestDTO requestDTO);

    void deleteUser(Long id);

    Page<UserDTO> getAllUsers(Pageable pageable);

    // Optional: search
    // List<UserDTO> searchUsers(String keyword);
}
