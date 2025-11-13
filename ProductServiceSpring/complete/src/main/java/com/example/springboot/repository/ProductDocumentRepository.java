package com.example.springboot.repository;

import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.example.springboot.dto.ProductDTO;
import com.example.springboot.entity.ProductDocument;
import org.hibernate.procedure.ProcedureOutputs;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductDocumentRepository<T> {

//    List<ProductDocument> findByNameContaining(String name);
//
//    List<ProductDocument> findByDescriptionContaining(String description);

    UpdateResponse<ProductDocument> saveProduct(ProductDocument productDocument);

}
