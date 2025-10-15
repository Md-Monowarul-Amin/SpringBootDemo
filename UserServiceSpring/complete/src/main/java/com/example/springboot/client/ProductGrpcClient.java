package com.example.springboot.client;

import com.example.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductGrpcClient {

    @GrpcClient("product-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

    public ProductResponse createProduct(String name, String description, double price) {
        CreateProductRequest request = CreateProductRequest.newBuilder()
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .build();
        
        return productServiceStub.createProduct(request);
    }

    public ProductResponse getProduct(String id) {
        GetProductRequest request = GetProductRequest.newBuilder()
                .setId(id)
                .build();
        
        return productServiceStub.getProduct(request);
    }

    public ProductListResponse getAllProducts() {
        EmptyRequest request = EmptyRequest.newBuilder().build();
        return productServiceStub.getAllProducts(request);
    }

    public ProductResponse updateProduct(String id, String name, String description, double price) {
        UpdateProductRequest request = UpdateProductRequest.newBuilder()
                .setId(id)
                .setName(name)
                .setDescription(description)
                .setPrice(price)
                .build();
        
        return productServiceStub.updateProduct(request);
    }

    public DeleteProductResponse deleteProduct(String id) {
        DeleteProductRequest request = DeleteProductRequest.newBuilder()
                .setId(id)
                .build();
        
        return productServiceStub.deleteProduct(request);
    }
}