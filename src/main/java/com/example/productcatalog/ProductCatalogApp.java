package com.example.productcatalog;

import com.example.productcatalog.controller.ProductCatalogController;
import com.example.productcatalog.repository.mongo.ProductMongoRepository;
import com.example.productcatalog.view.swing.ProductCatalogSwingView;

import javax.swing.SwingUtilities;

public class ProductCatalogApp {

    private static String mongoHost = "localhost";
    private static int mongoPort = 27017;
    private static String databaseName = "productcatalog";
    private static String collectionName = "products";

    public static void main(String[] args) {
        // Parse command line arguments
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--mongo-host":
                    mongoHost = args[++i];
                    break;
                case "--mongo-port":
                    mongoPort = Integer.parseInt(args[++i]);
                    break;
                case "--db-name":
                    databaseName = args[++i];
                    break;
                case "--db-collection":
                    collectionName = args[++i];
                    break;
            }
        }

        System.out.println("Starting Product Catalog...");
        System.out.println("Connecting to MongoDB at " + mongoHost + ":" + mongoPort);

        // Use invokeLater to run GUI on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                String connectionString = "mongodb://" + mongoHost + ":" + mongoPort;

                ProductMongoRepository productRepo =
                    new ProductMongoRepository(connectionString, databaseName);

                ProductCatalogSwingView view = new ProductCatalogSwingView();

                ProductCatalogController controller =
                    new ProductCatalogController(productRepo, view);

                view.setProductCatalogController(controller);
                view.setVisible(true);
                controller.allProducts();

                System.out.println("Application started!");

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
