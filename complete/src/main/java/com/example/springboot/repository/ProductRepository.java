package com.example.springboot.repository;

import com.example.springboot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;


public interface ProductRepository extends JpaRepository<Product, UUID>{

}