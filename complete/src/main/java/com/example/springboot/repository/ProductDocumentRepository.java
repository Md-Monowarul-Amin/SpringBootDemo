package com.example.springboot.repository;

import com.example.springboot.entity.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ProductDocumentRepository extends ElasticsearchRepository<ProductDocument, UUID> {

    List<ProductDocument> findByNameContaining(String name);

    List<ProductDocument> findByDescriptionContaining(String description);
}
