package com.example.productcatalog.controller;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.repository.ProductRepository;
import com.example.productcatalog.view.ProductCatalogView;

import java.util.List;

public class ProductCatalogController {

    private final ProductRepository productRepository;
    private final ProductCatalogView view;

    public ProductCatalogController(ProductRepository productRepository,
                                    ProductCatalogView view) {
        this.productRepository = productRepository;
        this.view = view;
    }

    public void allProducts() {
        List<Product> products = productRepository.findAll();
        view.showAllProducts(products);
    }

    public void newProduct(Product product) {
        Product existing = productRepository.findById(product.getId());
        if (existing != null) {
            view.showError("Already existing product with id " + product.getId(), existing);
            return;
        }
        productRepository.save(product);
        view.productAdded(product);
    }

    public void deleteProduct(Product product) {
        Product existing = productRepository.findById(product.getId());
        if (existing == null) {
            view.showError("No existing product with id " + product.getId(), product);
            return;
        }
        productRepository.delete(product.getId());
        view.productRemoved(product);
    }
}