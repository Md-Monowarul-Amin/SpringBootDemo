package com.example.springboot.service;

import com.example.springboot.dto.UserCreateRequestDTO;
import com.example.springboot.dto.UserDTO;
import com.example.springboot.dto.UserUpdateRequestDTO;
import com.example.springboot.grpc.*;
import com.example.springboot.mapper.UserGrpcMapper;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class UserGrpcServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserService userService;
    private final UserGrpcMapper grpcMapper;

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            log.info("gRPC: Creating user with username: {}", request.getUsername());
            
            UserCreateRequestDTO requestDTO = grpcMapper.toCreateRequestDTO(request);
            UserDTO userDTO = userService.createUser(requestDTO);
            UserResponse response = grpcMapper.toGrpcResponse(userDTO);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC: User created successfully with ID: {}", userDTO.getId());
        } catch (Exception e) {
            log.error("gRPC: Error creating user", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            log.info("gRPC: Fetching user with ID: {}", request.getId());
            
            UserDTO userDTO = userService.getUserById(request.getId());
            UserResponse response = grpcMapper.toGrpcResponse(userDTO);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC: User fetched successfully");
        } catch (Exception e) {
            log.error("gRPC: Error fetching user", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void updateUser(UpdateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            log.info("gRPC: Updating user with ID: {}", request.getId());
            
            UserUpdateRequestDTO requestDTO = grpcMapper.toUpdateRequestDTO(request);
            UserDTO userDTO = userService.updateUser(request.getId(), requestDTO);
            UserResponse response = grpcMapper.toGrpcResponse(userDTO);
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC: User updated successfully");
        } catch (Exception e) {
            log.error("gRPC: Error updating user", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public void deleteUser(DeleteUserRequest request, StreamObserver<DeleteUserResponse> responseObserver) {
        try {
            log.info("gRPC: Deleting user with ID: {}", request.getId());
            
            userService.deleteUser(request.getId());
            
            DeleteUserResponse response = DeleteUserResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("User deleted successfully")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC: User deleted successfully");
        } catch (Exception e) {
            log.error("gRPC: Error deleting user", e);
            
            DeleteUserResponse response = DeleteUserResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void listUsers(ListUsersRequest request, StreamObserver<UserListResponse> responseObserver) {
        try {
            log.info("gRPC: Listing users - page: {}, size: {}", request.getPage(), request.getSize());
            
            int page = request.getPage() > 0 ? request.getPage() - 1 : 0;
            int size = request.getSize() > 0 ? request.getSize() : 10;
            
            Page<UserDTO> userPage = userService.getAllUsers(PageRequest.of(page, size));
            
            List<UserResponse> userResponses = userPage.getContent().stream()
                    .map(grpcMapper::toGrpcResponse)
                    .collect(Collectors.toList());
            
            UserListResponse response = UserListResponse.newBuilder()
                    .addAllUsers(userResponses)
                    .setTotalPages(userPage.getTotalPages())
                    .setTotalElements(userPage.getTotalElements())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            log.info("gRPC: Users listed successfully");
        } catch (Exception e) {
            log.error("gRPC: Error listing users", e);
            responseObserver.onError(e);
        }
    }
}