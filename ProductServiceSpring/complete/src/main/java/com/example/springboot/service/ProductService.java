package com.example.springboot.service;

import com.example.springboot.dto.ProductDTO;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    ProductDTO getProductById(UUID id);
    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(UUID id, ProductDTO productDTO);
    void deleteProduct(UUID id);
    List<ProductDTO> search(String keyword);
}
