package com.example.productcatalog.repository.mongo;

import com.example.productcatalog.model.Product;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

import static org.junit.Assert.*;

public class ProductMongoRepositoryTest {

    private MongoDBContainer mongoDBContainer;
    private ProductMongoRepository repository;
    private MongoClient mongoClient;

    @Before
    public void setUp() {
        mongoDBContainer = new MongoDBContainer("mongo:6.0");
        mongoDBContainer.start();
        mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
        repository = new ProductMongoRepository(mongoClient, "testdb");
    }

    @After
    public void tearDown() {
        if (mongoClient != null) {
            mongoClient.close();
        }
        if (mongoDBContainer != null && mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
        }
    }

    @Test
    public void testSaveAndFindAll() {
        Product product = new Product("1", "Laptop", 999.99, "cat1");
        repository.save(product);
        List<Product> products = repository.findAll();
        assertEquals(1, products.size());
        assertEquals("Laptop", products.get(0).getName());
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product("2", "Mouse", 29.99, "cat1");
        repository.save(product);
        repository.delete("2");
        List<Product> products = repository.findAll();
        assertTrue(products.isEmpty());
    }

    @Test
    public void testFindById() {
        Product product = new Product("3", "Keyboard", 49.99, "cat1");
        repository.save(product);
        Product found = repository.findById("3");
        assertNotNull(found);
        assertEquals("Keyboard", found.getName());
    }

    @Test
    public void testFindByIdNotFound() {
        Product found = repository.findById("notexist");
        assertNull(found);
    }

    @Test
    public void testStringConstructorCreatesRepository() {
        String connectionString = mongoDBContainer.getReplicaSetUrl();
        ProductMongoRepository repo =
            new ProductMongoRepository(connectionString, "testdb2");
        assertNotNull(repo);
        assertNotNull(repo.getMongoClient());
        repo.getMongoClient().close();
    }
}
