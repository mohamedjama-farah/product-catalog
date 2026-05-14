package com.example.productcatalog;

import com.example.productcatalog.controller.ProductCatalogController;
import com.example.productcatalog.repository.mongo.ProductMongoRepository;
import com.example.productcatalog.view.swing.ProductCatalogSwingView;

import javax.swing.SwingUtilities;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductCatalogApp {

    private static final Logger LOGGER =
        Logger.getLogger(ProductCatalogApp.class.getName());

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 27017;
    private static final String DEFAULT_DB = "productcatalog";

    public static void main(String[] args) {
        String mongoHost = DEFAULT_HOST;
        int mongoPort = DEFAULT_PORT;
        String databaseName = DEFAULT_DB;

        int i = 0;
        while (i < args.length) {
            switch (args[i]) {
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

        final String host = mongoHost;
        final int port = mongoPort;
        final String db = databaseName;

        LOGGER.info("Starting Product Catalog...");

        SwingUtilities.invokeLater(() -> {
            try {
                String connectionString = "mongodb://" + host + ":" + port;
                ProductMongoRepository productRepo =
                    new ProductMongoRepository(connectionString, db);
                ProductCatalogSwingView view = new ProductCatalogSwingView();
                ProductCatalogController controller =
                    new ProductCatalogController(productRepo, view);
                view.setProductCatalogController(controller);
                view.setVisible(true);
                controller.allProducts();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error starting application", e);
            }
        });
    }
}
