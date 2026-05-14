package com.example.productcatalog.repository.mongo;

import com.example.productcatalog.model.Category;
import com.example.productcatalog.repository.CategoryRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class CategoryMongoRepository implements CategoryRepository {

    private final MongoClient mongoClient;
    private final MongoCollection<Document> collection;

    public CategoryMongoRepository(String connectionString, String databaseName) {
        this.mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = this.mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection("categories");
    }

    public CategoryMongoRepository(MongoClient mongoClient, String databaseName) {
        this.mongoClient = mongoClient;
        MongoDatabase database = this.mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection("categories");
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    @Override
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        for (Document doc : collection.find()) {
            categories.add(documentToCategory(doc));
        }
        return categories;
    }

    @Override
    public Category findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToCategory(doc) : null;
    }

    @Override
    public Category save(Category category) {
        Document doc = new Document("_id", category.getId())
                .append("name", category.getName());
        collection.replaceOne(
            Filters.eq("_id", category.getId()), doc,
            new ReplaceOptions().upsert(true));
        return category;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Category documentToCategory(Document doc) {
        return new Category(
            doc.getString("_id"),
            doc.getString("name")
        );
    }
}
