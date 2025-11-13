package com.example.springboot.controller;

import com.example.springboot.config.RabbitMQConfig;
import com.example.springboot.entity.Product;
import com.example.springboot.dto.ProductDTO;
import com.example.springboot.rabbitMQ.producer.ProductProducer;
import com.example.springboot.service.ProductService;
import com.example.springboot.service.ProductServiceImpl;

import static com.example.springboot.utils.Constants.TEST_NAME;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final String testName;
    private ProductProducer productProducer;


    public ProductController(ProductServiceImpl productService, @Value("${test.name}") String testName, ProductProducer productProducer) {

        this.productService = productService;
        this.testName = testName;
        this.productProducer = productProducer;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Enhanced search endpoint
    @GetMapping("/search")
    public List<ProductDTO> search(@RequestParam String keyword) {
        return productService.search(keyword);
    }

    @GetMapping("/test-environment-variable")
    public String getEnvironmentVariable() {
        return this.testName;
    }

    @GetMapping("/send-message")
    public String sendMessage(@RequestParam String message) {
        this.productProducer.sendMessage(message);
        return message + "sent successfully";
    }
}
