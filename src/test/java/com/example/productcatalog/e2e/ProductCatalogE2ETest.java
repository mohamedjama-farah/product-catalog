package com.example.productcatalog.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import javax.swing.JFrame;

@RunWith(GUITestRunner.class)
public class ProductCatalogE2ETest extends AssertJSwingJUnitTestCase {

    @ClassRule
    public static final MongoDBContainer mongo =
        new MongoDBContainer("mongo:4.4.3");

    private static MongoClient mongoClient;
    private static int mongoPort;

    private FrameFixture window;

    @org.junit.BeforeClass
    public static void setupMongo() {
        mongoPort = mongo.getFirstMappedPort();
        mongoClient = MongoClients.create(
            "mongodb://" + mongo.getHost() + ":" + mongoPort);
        MongoDatabase db = mongoClient.getDatabase("productcatalog");
        db.drop();
    }

    @org.junit.BeforeClass
    public static void tearDownMongo() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    protected void onSetUp() {
        com.example.productcatalog.ProductCatalogApp.main(new String[] {
            "--mongo-host", mongo.getHost(),
            "--mongo-port", String.valueOf(mongoPort),
            "--db-name", "productcatalog"
        });

        window = WindowFinder.findFrame(
            new GenericTypeMatcher<JFrame>(JFrame.class) {
                @Override
                protected boolean isMatching(JFrame frame) {
                    return "Product Catalog".equals(frame.getTitle()) 
                        && frame.isShowing();
                }
            }).withTimeout(30000).using(robot());
    }

    @Override
    public void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }

    @Test
    public void testAppStartsAndShowsList() {
        assertThat(window.target().getTitle()).isEqualTo("Product Catalog");
        window.list("productList").requireVisible();
    }

    @Test
    public void testAddProduct() {
        window.textBox("idTextBox").enterText("777");
        window.textBox("nameTextBox").enterText("TestProduct");
        window.textBox("priceTextBox").enterText("149.99");
        window.textBox("categoryIdTextBox").enterText("e2e");
        window.button(JButtonMatcher.withText("Add")).click();
        String[] contents = window.list("productList").contents();
        assertThat(contents)
            .anySatisfy(s -> assertThat(s).contains("777", "TestProduct"));
    }
}
