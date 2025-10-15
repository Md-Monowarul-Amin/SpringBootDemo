package com.example.springboot.service;

import com.example.grpc.*;
import com.example.springboot.entity.Product;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.mapper.ProductGrpcMapper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class ProductGrpcServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductGrpcMapper mapper;

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            Product product = new Product(
                    request.getName(),
                    request.getDescription(),
                    request.getPrice()
            );
            
            Product savedProduct = productRepository.save(product);
            
            ProductMessage productMessage = ProductMessage.newBuilder()
                    .setId(savedProduct.getId().toString())
                    .setName(savedProduct.getName())
                    .setDescription(savedProduct.getDescription())
                    .setPrice(savedProduct.getPrice())
                    .build();
            
            ProductResponse response = ProductResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Product created successfully")
                    .setProduct(productMessage)
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            ProductResponse response = ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error creating product: " + e.getMessage())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getProduct(GetProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            UUID productId = UUID.fromString(request.getId());
            Optional<Product> productOptional = productRepository.findById(productId);
            
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                
                ProductMessage productMessage = ProductMessage.newBuilder()
                        .setId(product.getId().toString())
                        .setName(product.getName())
                        .setDescription(product.getDescription())
                        .setPrice(product.getPrice())
                        .build();
                
                ProductResponse response = ProductResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Product found")
                        .setProduct(productMessage)
                        .build();
                
                responseObserver.onNext(response);
            } else {
                ProductResponse response = ProductResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Product not found with ID: " + request.getId())
                        .build();
                
                responseObserver.onNext(response);
            }
            
            responseObserver.onCompleted();
            
        } catch (IllegalArgumentException e) {
            ProductResponse response = ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Invalid product ID format")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            ProductResponse response = ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving product: " + e.getMessage())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void getAllProducts(EmptyRequest request, StreamObserver<ProductListResponse> responseObserver) {
        try {
            List<Product> products = productRepository.findAll();
            
            ProductListResponse.Builder responseBuilder = ProductListResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Products retrieved successfully");
            
            for (Product product : products) {
                ProductMessage productMessage = ProductMessage.newBuilder()
                        .setId(product.getId().toString())
                        .setName(product.getName())
                        .setDescription(product.getDescription())
                        .setPrice(product.getPrice())
                        .build();
                
                responseBuilder.addProducts(productMessage);
            }
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            ProductListResponse response = ProductListResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error retrieving products: " + e.getMessage())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateProduct(UpdateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        try {
            UUID productId = UUID.fromString(request.getId());
            Optional<Product> productOptional = productRepository.findById(productId);
            
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setName(request.getName());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());
                
                Product updatedProduct = productRepository.save(product);
                
                ProductMessage productMessage = ProductMessage.newBuilder()
                        .setId(updatedProduct.getId().toString())
                        .setName(updatedProduct.getName())
                        .setDescription(updatedProduct.getDescription())
                        .setPrice(updatedProduct.getPrice())
                        .build();
                
                ProductResponse response = ProductResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Product updated successfully")
                        .setProduct(productMessage)
                        .build();
                
                responseObserver.onNext(response);
            } else {
                ProductResponse response = ProductResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Product not found with ID: " + request.getId())
                        .build();
                
                responseObserver.onNext(response);
            }
            
            responseObserver.onCompleted();
            
        } catch (IllegalArgumentException e) {
            ProductResponse response = ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Invalid product ID format")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            ProductResponse response = ProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error updating product: " + e.getMessage())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteProduct(DeleteProductRequest request, StreamObserver<DeleteProductResponse> responseObserver) {
        try {
            UUID productId = UUID.fromString(request.getId());
            
            if (productRepository.existsById(productId)) {
                productRepository.deleteById(productId);
                
                DeleteProductResponse response = DeleteProductResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage("Product deleted successfully")
                        .build();
                
                responseObserver.onNext(response);
            } else {
                DeleteProductResponse response = DeleteProductResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Product not found with ID: " + request.getId())
                        .build();
                
                responseObserver.onNext(response);
            }
            
            responseObserver.onCompleted();
            
        } catch (IllegalArgumentException e) {
            DeleteProductResponse response = DeleteProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Invalid product ID format")
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            DeleteProductResponse response = DeleteProductResponse.newBuilder()
                    .setSuccess(false)
                    .setMessage("Error deleting product: " + e.getMessage())
                    .build();
            
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }
}