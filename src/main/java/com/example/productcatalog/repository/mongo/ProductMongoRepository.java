package com.example.productcatalog.repository.mongo;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.repository.ProductRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ProductMongoRepository implements ProductRepository {

    private final MongoCollection<Document> collection;

    public ProductMongoRepository(String connectionString, String databaseName) {
        MongoClient mongoClient = MongoClients.create(connectionString);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection("products");
    }

    public ProductMongoRepository(MongoClient mongoClient, String databaseName) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        this.collection = database.getCollection("products");
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        for (Document doc : collection.find()) {
            products.add(documentToProduct(doc));
        }
        return products;
    }

    @Override
    public Product findById(String id) {
        Document doc = collection.find(Filters.eq("_id", id)).first();
        return doc != null ? documentToProduct(doc) : null;
    }

    @Override
    public Product save(Product product) {
        Document doc = new Document("_id", product.getId())
                .append("name", product.getName())
                .append("price", product.getPrice())
                .append("categoryId", product.getCategoryId());
        collection.replaceOne(
            Filters.eq("_id", product.getId()), doc,
            new ReplaceOptions().upsert(true));
        return product;
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    private Product documentToProduct(Document doc) {
        return new Product(
            doc.getString("_id"),
            doc.getString("name"),
            doc.getDouble("price"),
            doc.getString("categoryId")
        );
    }
}
