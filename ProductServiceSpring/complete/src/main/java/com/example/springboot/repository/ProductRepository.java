package com.example.springboot.repository;

import com.example.springboot.dto.ProductDTO;
import com.example.springboot.entity.Product;
import com.example.springboot.entity.ProductDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface ProductRepository extends JpaRepository<Product, UUID>{
//    ProductDocument saveProduct(ProductDTO productDTO);
}