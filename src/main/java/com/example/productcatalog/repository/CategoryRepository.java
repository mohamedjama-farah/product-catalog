package com.example.productcatalog.repository;

import com.example.productcatalog.model.Category;
import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
    Category findById(String id);
    Category save(Category category);   // return the saved category
    void delete(String id);
}