package com.example.springboot.service;

import com.example.springboot.dto.UserCreateRequestDTO;
import com.example.springboot.dto.UserDTO;
import com.example.springboot.dto.UserUpdateRequestDTO;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import com.example.springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;  // maps entity ↔ DTO

    @Override
    public UserDTO createUser(UserCreateRequestDTO dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword()) // ⚠ Make sure this line exists!
                .build();

    // Save entity
    User savedUser = userRepository.save(user);

    // Map entity to DTO
    return UserDTO.builder()
            .id(savedUser.getId())
            .username(savedUser.getUsername())
            .email(savedUser.getEmail())
            .build();
    }
    
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        return userMapper.toDTO(user);
    }

    // @Override
    // public UserDTO getUserByUsername(String username) {
    //     User user = userRepository.findByUsername(username)
    //             .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    //     return userMapper.toDTO(user);
    // }

    @Override
    public UserDTO updateUser(Long id, UserUpdateRequestDTO requestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        userMapper.updateEntityFromDTO(requestDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
        userRepository.delete(existingUser);
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toDTO);
    }

    // @Override
    // public List<UserDTO> searchUsers(String keyword) {
    //     return userRepository.searchByKeyword(keyword).stream()
    //             .map(userMapper::toDTO)
    //             .collect(Collectors.toList());
    // }
}
