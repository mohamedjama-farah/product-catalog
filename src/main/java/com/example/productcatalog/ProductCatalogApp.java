package com.example.productcatalog;

import com.example.productcatalog.controller.ProductCatalogController;
import com.example.productcatalog.repository.mongo.ProductMongoRepository;
import com.example.productcatalog.view.swing.ProductCatalogSwingView;

import javax.swing.SwingUtilities;
import java.util.logging.Logger;

public class ProductCatalogApp {

    private static final Logger LOGGER = Logger.getLogger(ProductCatalogApp.class.getName());
    private static String mongoHost = "localhost";
    private static int mongoPort = 27017;
    private static String databaseName = "productcatalog";

    public static void main(String[] args) {
        parseArguments(args);

        LOGGER.info("Starting Product Catalog...");
        LOGGER.info("Connecting to MongoDB at " + mongoHost + ":" + mongoPort);

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

                LOGGER.info("Application started!");

            } catch (Exception e) {
                LOGGER.severe("Error: " + e.getMessage());
            }
        });
    }

    private static void parseArguments(String[] args) {
        int i = 0;
        while (i < args.length) {
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
                    // Ignored - collection name is handled in repository
                    ++i;
                    break;
                default:
                    ++i;
                    break;
            }
            i++;
        }
    }
}
