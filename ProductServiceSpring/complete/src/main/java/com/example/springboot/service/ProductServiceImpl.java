package com.example.springboot.service;

import com.example.springboot.dto.ProductDTO;
import com.example.springboot.entity.Product;
import com.example.springboot.entity.ProductDocument;
import com.example.springboot.repository.ProductRepository;
import com.example.springboot.repository.ProductDocumentRepository;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

// import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
// import org.elasticsearch.index.query.BoolQueryBuilder;
// import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_CACHE_PREFIX = "product"; // Redis key prefix

    private final ProductRepository productRepository;
    private final ProductDocumentRepository productDocumentRepository;
    private final ModelMapper modelMapper;
    private final ElasticsearchClient client;

    public ProductServiceImpl(ProductRepository productRepository,
            ProductDocumentRepository productDocumentRepository,
            ModelMapper modelMapper,
            ElasticsearchClient client) {
        this.productRepository = productRepository;
        this.productDocumentRepository = productDocumentRepository;
        this.modelMapper = modelMapper;
        this.client = client;
    }

    // Convert Product entity to ProductDTO
    private ProductDTO mapToDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    // Convert ProductDTO to Product entity
    private Product mapToEntity(ProductDTO productDTO) {
        return new Product(productDTO.getName(), productDTO.getDescription(), productDTO.getPrice());
    }

    private ProductDocument mapToDocument(Product product) {
        return new ProductDocument(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "product", key = "#id")
    public ProductDTO getProductById(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        ProductDTO productDto = modelMapper.map(product, ProductDTO.class);

        return productDto;
    }

    @Override
    @CachePut(value = "product", key = "#result.id")
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product savedProduct = productRepository.save(product);
        productDocumentRepository.save(modelMapper.map(productDTO, ProductDocument.class));

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    @CachePut(value = "product", key = "#id")
    public ProductDTO updateProduct(UUID id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Manually set fields to avoid overwriting ID
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());

        Product updatedProduct = productRepository.save(existingProduct);
        productDocumentRepository.save(modelMapper.map(updatedProduct, ProductDocument.class));

        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    @CacheEvict(value = "product", key = "#id")
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
        productDocumentRepository.deleteById(product.getId());
    }

    @Override
    public List<ProductDTO> search(String keyword) {
        try {
            SearchResponse<ProductDocument> response = client.search(s -> s
                    .index("products")
                    .query(q -> q
                            .multiMatch(m -> m
                                    .fields("name", "description")
                                    .query(keyword)
                                    .fuzziness("AUTO"))),
                    ProductDocument.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .map(doc -> modelMapper.map(doc, ProductDTO.class))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Elasticsearch search failed...!!", e);
        }

    }
}
