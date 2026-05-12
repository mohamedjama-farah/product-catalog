package com.example.productcatalog.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.annotation.GUITest;
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

import javax.swing.JFrame;

@RunWith(GUITestRunner.class)
public class ProductCatalogE2ETest extends AssertJSwingJUnitTestCase {

    @ClassRule
    public static final MongoDBContainer mongo =
        new MongoDBContainer("mongo:4.4.3");

    private FrameFixture window;

    @Override
    protected void onSetUp() {
        int port = mongo.getFirstMappedPort();
        String host = mongo.getHost();
        
        System.out.println("MongoDB running at: " + host + ":" + port);

        // Launch the app in a separate thread
        new Thread(() -> {
            com.example.productcatalog.ProductCatalogApp.main(new String[] {
                "--mongo-host", host,
                "--mongo-port", String.valueOf(port),
                "--db-name", "testdb",
                "--db-collection", "testcollection"
            });
        }).start();

        // Wait for the window to appear
        System.out.println("Waiting for window...");
        window = WindowFinder.findFrame(
            new GenericTypeMatcher<JFrame>(JFrame.class) {
                @Override
                protected boolean isMatching(JFrame frame) {
                    return "Product Catalog".equals(frame.getTitle()) 
                        && frame.isShowing();
                }
            }).withTimeout(30000).using(robot());
        
        System.out.println("Window found!");
    }

    @Override
    public void onTearDown() {
        if (window != null) {
            window.cleanUp();
        }
    }

    @Test @GUITest
    public void testAppStartsAndShowsList() {
        // Verify window is not null
        assertThat(window.target().getTitle()).isEqualTo("Product Catalog");
        
        // Verify list is accessible
        window.list("productList").requireVisible();
    }

    @Test @GUITest
    public void testAddProduct() {
        // Fill fields
        window.textBox("idTextBox").enterText("777");
        window.textBox("nameTextBox").enterText("E2EProduct");
        window.textBox("priceTextBox").enterText("149.99");
        window.textBox("categoryIdTextBox").enterText("e2e");

        // Click Add
        window.button(JButtonMatcher.withText("Add")).click();

        // Wait for processing
        try { Thread.sleep(1500); } catch (InterruptedException e) { }

        // Verify product appears in list
        String[] contents = window.list("productList").contents();
        assertThat(contents)
            .anySatisfy(s -> assertThat(s).contains("777", "E2EProduct"));
    }
}
