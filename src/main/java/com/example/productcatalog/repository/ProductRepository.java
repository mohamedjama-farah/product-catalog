package com.example.productcatalog.repository;

import com.example.productcatalog.model.Product;
import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    Product findById(String id);

    Product save(Product product);     // returns the saved product

    void delete(String id);            // or deleteById(String id)
}