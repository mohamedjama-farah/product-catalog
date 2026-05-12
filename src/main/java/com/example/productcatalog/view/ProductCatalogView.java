package com.example.productcatalog.view;

import com.example.productcatalog.model.Product;
import java.util.List;

public interface ProductCatalogView {
    void showAllProducts(List<Product> products);
    void showError(String message, Product product);
    void productAdded(Product product);
    void productRemoved(Product product);
}
