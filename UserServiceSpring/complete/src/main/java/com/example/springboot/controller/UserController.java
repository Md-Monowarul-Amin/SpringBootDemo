package com.example.springboot.controller;

import com.example.springboot.client.ProductGrpcClient;
import com.example.grpc.ProductListResponse;
import com.example.springboot.client.UserGrpcClient;
import com.example.springboot.dto.*;
import com.example.springboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProductGrpcClient productClient;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserDTO>> createUser(
            @Valid @RequestBody UserCreateRequestDTO requestDTO) {

        UserDTO userDTO = userService.createUser(requestDTO);
        ApiResponseDTO<UserDTO> response = ApiResponseDTO.<UserDTO>builder()
                .success(true)
                .message("User created successfully")
                .data(userDTO)
                .count(1)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        ApiResponseDTO<UserDTO> response = ApiResponseDTO.<UserDTO>builder()
                .success(true)
                .message("User retrieved successfully")
                .data(userDTO)
                .count(1)
                .build();

        return ResponseEntity.ok(response);
    }

    // @GetMapping("/username/{username}")
    // public ResponseEntity<ApiResponseDTO<UserDTO>> getUserByUsername(@PathVariable String username) {
    //     UserDTO userDTO = userService.getUserByUsername(username);
    //     ApiResponseDTO<UserDTO> response = ApiResponseDTO.<UserDTO>builder()
    //             .success(true)
    //             .message("User retrieved successfully")
    //             .data(userDTO)
    //             .count(1)
    //             .build();

    //     return ResponseEntity.ok(response);
    // }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserDTO>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO requestDTO) {

        UserDTO userDTO = userService.updateUser(id, requestDTO);
        ApiResponseDTO<UserDTO> response = ApiResponseDTO.<UserDTO>builder()
                .success(true)
                .message("User updated successfully")
                .data(userDTO)
                .count(1)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                .success(true)
                .message("User deleted successfully")
                .data(null)
                .count(0)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<UserDTO> userPage = userService.getAllUsers(PageRequest.of(page, size));

        UserResponseDTO responseDTO = new UserResponseDTO(
                userPage.getContent(),
                userPage.getNumber(),
                userPage.getTotalPages(),
                userPage.getTotalElements()
        );

        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.<UserResponseDTO>builder()
                .success(true)
                .message("Users retrieved successfully")
                .data(responseDTO)
                .count(userPage.getNumberOfElements())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProductListResponse products = productClient.getAllProducts();

        List<ProductResponseDTO> productList = products.getProductsList().stream()
                .map(p -> new ProductResponseDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getPrice()
                ))
                .toList();

        ApiResponseDTO<List<ProductResponseDTO>> response = ApiResponseDTO.<List<ProductResponseDTO>>builder()
                .success(true)
                .message("Products retrieved successfully")
                .data(productList)
                .count(productList.size()) 
                .build();

        return ResponseEntity.ok(response);
    }
}