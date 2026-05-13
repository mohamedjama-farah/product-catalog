package com.example.productcatalog;

import com.example.productcatalog.controller.ProductCatalogController;
import com.example.productcatalog.repository.mongo.ProductMongoRepository;
import com.example.productcatalog.view.swing.ProductCatalogSwingView;

import javax.swing.SwingUtilities;
import java.util.logging.Level;
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
                LOGGER.log(Level.SEVERE, "Error: " + e.getMessage(), e);
            }
        });
    }

    private static void parseArguments(String[] args) {
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            switch (arg) {
                case "--mongo-host":
                    mongoHost = args[i + 1];
                    i++;
                    break;
                case "--mongo-port":
                    mongoPort = Integer.parseInt(args[i + 1]);
                    i++;
                    break;
                case "--db-name":
                    databaseName = args[i + 1];
                    i++;
                    break;
                default:
                    break;
            }
            i++;
        }
    }
}
