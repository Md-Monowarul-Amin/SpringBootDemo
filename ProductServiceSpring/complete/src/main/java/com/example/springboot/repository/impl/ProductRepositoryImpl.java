package com.example.springboot.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.example.springboot.dto.ProductDTO;
import com.example.springboot.entity.ProductDocument;
import com.example.springboot.repository.ProductDocumentRepository;
import com.example.springboot.repository.ProductRepository;
import jakarta.annotation.Nonnull;

import static com.example.springboot.utils.Constants.PRODUCTS_INDEX;

public class ProductRepositoryImpl implements ProductDocumentRepository<ProductDocument> {

    @Nonnull
    private ElasticsearchClient elasticsearchClient;

    @Override
    public UpdateResponse<ProductDocument> saveProduct(ProductDocument productDocument) {
        String indexName = PRODUCTS_INDEX;
        try {
            UpdateResponse<? extends ProductDocument> response = elasticsearchClient.update(u -> u
                            .index(PRODUCTS_INDEX)
                            .id(productDocument.getId().toString())
                            .doc(productDocument)
                            .retryOnConflict(3)
                            .docAsUpsert(true),
                    productDocument.getClass());

            return (UpdateResponse<ProductDocument>) response;

        } catch (Exception e) {
            throw new RuntimeException("Elasticsearch saveOrUpdate failed for product ID: " + productDocument.getId(), e);
        }
    }
}
