package com.example.productcatalog.repository.mongo;

import com.example.productcatalog.model.Category;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryMongoRepositoryTest {

    private MongoDBContainer mongoDBContainer;
    private CategoryMongoRepository repository;
    private MongoClient mongoClient;

    @Before
    public void setUp() {
        mongoDBContainer = new MongoDBContainer("mongo:6.0");
        mongoDBContainer.start();
        mongoClient = MongoClients.create(mongoDBContainer.getReplicaSetUrl());
        repository = new CategoryMongoRepository(mongoClient, "testdb");
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
    public void testFindAllWhenEmpty() {
        List<Category> categories = repository.findAll();
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testSaveAndFindAll() {
        Category category = new Category("1", "Electronics");
        repository.save(category);
        List<Category> categories = repository.findAll();
        assertEquals(1, categories.size());
        assertEquals("Electronics", categories.get(0).getName());
    }

    @Test
    public void testFindById() {
        Category category = new Category("1", "Electronics");
        repository.save(category);
        Category found = repository.findById("1");
        assertNotNull(found);
        assertEquals("Electronics", found.getName());
    }

    @Test
    public void testFindByIdNotFound() {
        Category found = repository.findById("notexist");
        assertNull(found);
    }

    @Test
    public void testDeleteCategory() {
        Category category = new Category("1", "Electronics");
        repository.save(category);
        repository.delete("1");
        List<Category> categories = repository.findAll();
        assertTrue(categories.isEmpty());
    }

    @Test
    public void testStringConstructorCreatesRepository() {
        String connectionString = mongoDBContainer.getReplicaSetUrl();
        CategoryMongoRepository repo =
            new CategoryMongoRepository(connectionString, "testdb2");
        assertNotNull(repo);
        assertNotNull(repo.getMongoClient());
        repo.getMongoClient().close();
    }
}
