package com.example.productcatalog.controller;

import static org.mockito.Mockito.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.repository.ProductRepository;
import com.example.productcatalog.view.ProductCatalogView;

public class ProductCatalogControllerTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCatalogView productCatalogView;

    @InjectMocks
    private ProductCatalogController productCatalogController;

    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void testAllProducts() {
        Product product1 = new Product("1", "Laptop", 999.99, "cat1");
        Product product2 = new Product("2", "Phone", 499.99, "cat2");
        when(productRepository.findAll()).thenReturn(asList(product1, product2));

        productCatalogController.allProducts();

        verify(productCatalogView).showAllProducts(asList(product1, product2));
    }

    @Test
    public void testAllProductsWhenEmpty() {
        when(productRepository.findAll()).thenReturn(emptyList());

        productCatalogController.allProducts();

        verify(productCatalogView).showAllProducts(emptyList());
    }

    @Test
    public void testNewProductWhenProductDoesNotAlreadyExist() {
        Product product = new Product("1", "Tablet", 299.99, "cat1");
        when(productRepository.findById("1")).thenReturn(null);

        productCatalogController.newProduct(product);

        InOrder inOrder = inOrder(productRepository, productCatalogView);
        inOrder.verify(productRepository).save(product);
        inOrder.verify(productCatalogView).productAdded(product);
    }

    @Test
    public void testNewProductWhenProductAlreadyExists() {
        Product productToAdd = new Product("1", "Tablet", 299.99, "cat1");
        Product existingProduct = new Product("1", "Laptop", 999.99, "cat1");
        when(productRepository.findById("1")).thenReturn(existingProduct);

        productCatalogController.newProduct(productToAdd);

        verify(productCatalogView)
            .showError("Already existing product with id 1", existingProduct);
        verifyNoMoreInteractions(ignoreStubs(productRepository));
    }

    @Test
    public void testDeleteProductWhenProductExists() {
        Product productToDelete = new Product("1", "Laptop", 999.99, "cat1");
        when(productRepository.findById("1")).thenReturn(productToDelete);

        productCatalogController.deleteProduct(productToDelete);

        InOrder inOrder = inOrder(productRepository, productCatalogView);
        inOrder.verify(productRepository).delete("1");
        inOrder.verify(productCatalogView).productRemoved(productToDelete);
    }

    @Test
    public void testDeleteProductWhenProductDoesNotExist() {
        Product product = new Product("1", "Laptop", 999.99, "cat1");
        when(productRepository.findById("1")).thenReturn(null);

        productCatalogController.deleteProduct(product);

        verify(productCatalogView)
            .showError("No existing product with id 1", product);
        verifyNoMoreInteractions(ignoreStubs(productRepository));
    }
}
