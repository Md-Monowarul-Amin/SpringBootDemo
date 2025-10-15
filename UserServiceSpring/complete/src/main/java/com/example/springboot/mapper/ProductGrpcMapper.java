package com.example.springboot.mapper;

import com.example.grpc.ProductMessage;
import com.example.grpc.ProductResponse;
import com.example.grpc.ProductListResponse;
import com.example.grpc.DeleteProductResponse;
import com.example.springboot.dto.ApiResponseDTO;
import com.example.springboot.dto.ProductResponseDTO;
import com.example.springboot.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductGrpcMapper {

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Convert Product entity to gRPC ProductMessage
     */
    public ProductMessage toProductMessage(Product product) {
        return ProductMessage.newBuilder()
                .setId(product.getId().toString())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setPrice(product.getPrice())
                .build();
    }

    /**
     * Convert gRPC ProductMessage to ProductResponseDTO
     */
    public ProductResponseDTO productMessageToDTO(ProductMessage productMessage) {
        return ProductResponseDTO.builder()
                .id(productMessage.getId())
                .name(productMessage.getName())
                .description(productMessage.getDescription())
                .price(productMessage.getPrice())
                .build();
    }

    /**
     * Convert gRPC ProductResponse to ApiResponseDTO<ProductResponseDTO>
     */
    public ApiResponseDTO<ProductResponseDTO> toApiResponse(ProductResponse grpcResponse) {
        ProductResponseDTO productDTO = null;
        
        if (grpcResponse.hasProduct()) {
            productDTO = productMessageToDTO(grpcResponse.getProduct());
        }
        
        return ApiResponseDTO.<ProductResponseDTO>builder()
                .success(grpcResponse.getSuccess())
                .message(grpcResponse.getMessage())
                .data(productDTO)
                .build();
    }

    /**
     * Convert gRPC ProductListResponse to ApiResponseDTO<List<ProductResponseDTO>>
     */
    public ApiResponseDTO<List<ProductResponseDTO>> toApiResponseList(ProductListResponse grpcResponse) {
        List<ProductResponseDTO> products = grpcResponse.getProductsList().stream()
                .map(this::productMessageToDTO)
                .collect(Collectors.toList());
        
        return ApiResponseDTO.<List<ProductResponseDTO>>builder()
                .success(grpcResponse.getSuccess())
                .message(grpcResponse.getMessage())
                .data(products)
                .count(products.size())
                .build();
    }

    /**
     * Convert gRPC DeleteProductResponse to ApiResponseDTO<Void>
     */
    public ApiResponseDTO<Void> toApiResponseVoid(DeleteProductResponse grpcResponse) {
        return ApiResponseDTO.<Void>builder()
                .success(grpcResponse.getSuccess())
                .message(grpcResponse.getMessage())
                .build();
    }

    /**
     * Convert Product entity to ProductResponseDTO (for traditional REST API)
     */
    public ProductResponseDTO productEntityToDTO(Product product) {
        return modelMapper.map(product, ProductResponseDTO.class);
    }

    /**
     * Convert List of Product entities to List of ProductResponseDTO
     */
    public List<ProductResponseDTO> productEntityListToDTO(List<Product> products) {
        return products.stream()
                .map(this::productEntityToDTO)
                .collect(Collectors.toList());
    }
}