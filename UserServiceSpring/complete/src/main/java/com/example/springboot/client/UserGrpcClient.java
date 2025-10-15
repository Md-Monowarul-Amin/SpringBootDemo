package com.example.springboot.client;

import com.example.springboot.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UserGrpcClient {

    private final UserServiceGrpc.UserServiceBlockingStub blockingStub;

    public UserGrpcClient() {
        // Replace "localhost" and 9090 with your gRPC server host & port
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext() // remove this for TLS
                .build();

        blockingStub = UserServiceGrpc.newBlockingStub(channel);
    }

    // Create user
    public UserResponse createUser(String username, String email, String password) {
        CreateUserRequest request = CreateUserRequest.newBuilder()
                .setUsername(username)
                .setEmail(email)
                .setPassword(password)
                .build();

        UserResponse response = blockingStub.createUser(request);
        log.info("Created user: {}", response.getId());
        return response;
    }

    // Get user by ID
    public UserResponse getUser(String id) {
        GetUserRequest request = GetUserRequest.newBuilder()
                .setId(Long.parseLong(id))
                .build();

        return blockingStub.getUser(request);
    }

    // Update user
    public UserResponse updateUser(String id, String username, String email, String password) {
        UpdateUserRequest request = UpdateUserRequest.newBuilder()
                .setId(Long.parseLong(id))
                .setUsername(username)
                .setEmail(email)
                .build();

        return blockingStub.updateUser(request);
    }

    // Delete user
    public DeleteUserResponse deleteUser(String id) {
        DeleteUserRequest request = DeleteUserRequest.newBuilder()
                .setId(Long.parseLong(id))
                .build();

        return blockingStub.deleteUser(request);
    }

    // List users with pagination
    public UserListResponse listUsers(int page, int size) {
        ListUsersRequest request = ListUsersRequest.newBuilder()
                .setPage(page)
                .setSize(size)
                .build();

        return blockingStub.listUsers(request);
    }
}
