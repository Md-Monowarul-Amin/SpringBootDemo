package com.example.springboot.controller;

import com.example.grpc.DeleteProductResponse;
import com.example.grpc.ProductListResponse;
import com.example.grpc.ProductResponse;
import com.example.springboot.client.ProductGrpcClient;
import com.example.springboot.dto.ApiResponseDTO;
import com.example.springboot.dto.ProductCreateRequestDTO;
import com.example.springboot.dto.ProductResponseDTO;
import com.example.springboot.dto.ProductUpdateRequestDTO;
import com.example.springboot.mapper.ProductGrpcMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grpc/products")
public class ProductGrpcController {

    @Autowired
    private ProductGrpcClient productGrpcClient;

    @Autowired
    private ProductGrpcMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> createProduct(
            @RequestBody ProductCreateRequestDTO request) {
        
        ProductResponse grpcResponse = productGrpcClient.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice()
        );
        
        ApiResponseDTO<ProductResponseDTO> apiResponse = mapper.toApiResponse(grpcResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getProduct(@PathVariable String id) {
        ProductResponse grpcResponse = productGrpcClient.getProduct(id);
        ApiResponseDTO<ProductResponseDTO> apiResponse = mapper.toApiResponse(grpcResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProductResponseDTO>>> getAllProducts() {
        ProductListResponse grpcResponse = productGrpcClient.getAllProducts();
        ApiResponseDTO<List<ProductResponseDTO>> apiResponse = mapper.toApiResponseList(grpcResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> updateProduct(
            @PathVariable String id,
            @RequestBody ProductUpdateRequestDTO request) {
        
        ProductResponse grpcResponse = productGrpcClient.updateProduct(
                id,
                request.getName(),
                request.getDescription(),
                request.getPrice()
        );
        
        ApiResponseDTO<ProductResponseDTO> apiResponse = mapper.toApiResponse(grpcResponse);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteProduct(@PathVariable String id) {
        DeleteProductResponse grpcResponse = productGrpcClient.deleteProduct(id);
        ApiResponseDTO<Void> apiResponse = mapper.toApiResponseVoid(grpcResponse);
        return ResponseEntity.ok(apiResponse);
    }
}