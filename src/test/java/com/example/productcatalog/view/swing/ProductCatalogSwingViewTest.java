package com.example.productcatalog.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.timeout;

import java.util.Arrays;
import javax.swing.DefaultListModel;

import org.assertj.swing.edt.GuiActionRunnable;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.productcatalog.controller.ProductCatalogController;
import com.example.productcatalog.model.Product;

@RunWith(GUITestRunner.class)
public class ProductCatalogSwingViewTest extends AssertJSwingJUnitTestCase {

    private FrameFixture window;
    private ProductCatalogSwingView productCatalogSwingView;

    @Mock
    private ProductCatalogController productCatalogController;

    private AutoCloseable closeable;

    private static final long TIMEOUT = 5000;

    @Override
    protected void onSetUp() {
        closeable = MockitoAnnotations.openMocks(this);
        GuiActionRunner.execute((GuiActionRunnable) () -> {
            productCatalogSwingView = new ProductCatalogSwingView();
            productCatalogSwingView.setProductCatalogController(productCatalogController);
        });
        window = new FrameFixture(robot(), productCatalogSwingView);
        window.show();
    }

    @Override
    public void onTearDown() {
        if (closeable != null) {
            try { closeable.close(); } catch (Exception e) { }
        }
        if (window != null) {
            window.cleanUp();
        }
    }

    @Test
    public void testControlsInitialStates() {
        window.textBox("idTextBox").requireEnabled();
        window.textBox("nameTextBox").requireEnabled();
        window.textBox("priceTextBox").requireEnabled();
        window.textBox("categoryIdTextBox").requireEnabled();
        window.list("productList");
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testWhenAllFieldsAreNonEmptyThenAddButtonShouldBeEnabled() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Laptop");
        window.textBox("priceTextBox").enterText("999.99");
        window.textBox("categoryIdTextBox").enterText("cat1");
        assertThat(window).isNotNull();
    }

    @Test
    public void testWhenAFieldIsBlankThenAddButtonShouldBeDisabled() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText(" ");
        window.textBox("priceTextBox").enterText("999.99");
        window.textBox("categoryIdTextBox").enterText("cat1");
        assertThat(window).isNotNull();
    }

    @Test
    public void testDeleteButtonEnabledWhenProductSelected() {
        GuiActionRunner.execute((GuiActionRunnable) () ->
            productCatalogSwingView.getListProductsModel()
                .addElement(new Product("1", "Laptop", 999.99, "cat1"))
        );
        window.list("productList").selectItem(0);
        assertThat(window).isNotNull();
    }

    @Test
    public void testsShowAllProductsShouldAddProductsToTheList() {
        Product p1 = new Product("1", "Laptop", 999.99, "cat1");
        Product p2 = new Product("2", "Phone", 499.99, "cat2");
        GuiActionRunner.execute((GuiActionRunnable) () ->
            productCatalogSwingView.showAllProducts(Arrays.asList(p1, p2))
        );
        String[] listContents = window.list().contents();
        assertThat(listContents).containsExactly(p1.toString(), p2.toString());
    }

    @Test
    public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
        Product product = new Product("1", "Laptop", 999.99, "cat1");
        GuiActionRunner.execute((GuiActionRunnable) () ->
            productCatalogSwingView.showError("error message", product)
        );
        window.label("errorMessageLabel")
            .requireText("error message: " + product);
    }

    @Test
    public void testProductAddedShouldAddToTheListAndResetErrorLabel() {
        Product product = new Product("1", "Laptop", 999.99, "cat1");
        GuiActionRunner.execute((GuiActionRunnable) () ->
            productCatalogSwingView.productAdded(product)
        );
        String[] listContents = window.list().contents();
        assertThat(listContents).containsExactly(product.toString());
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testProductRemovedShouldRemoveFromTheListAndResetErrorLabel() {
        Product p1 = new Product("1", "Laptop", 999.99, "cat1");
        Product p2 = new Product("2", "Phone", 499.99, "cat2");
        GuiActionRunner.execute((GuiActionRunnable) () -> {
            DefaultListModel<Product> model = productCatalogSwingView.getListProductsModel();
            model.addElement(p1);
            model.addElement(p2);
        });
        GuiActionRunner.execute((GuiActionRunnable) () ->
            productCatalogSwingView.productRemoved(p1)
        );
        String[] listContents = window.list().contents();
        assertThat(listContents).containsExactly(p2.toString());
        window.label("errorMessageLabel").requireText(" ");
    }

    @Test
    public void testAddButtonShouldDelegateToControllerNewProduct() {
        window.textBox("idTextBox").enterText("1");
        window.textBox("nameTextBox").enterText("Laptop");
        window.textBox("priceTextBox").enterText("999.99");
        window.textBox("categoryIdTextBox").enterText("cat1");
        window.button(JButtonMatcher.withText("Add")).click();
        verify(productCatalogController, timeout(TIMEOUT))
            .newProduct(new Product("1", "Laptop", 999.99, "cat1"));
    }

    @Test
    public void testDeleteButtonShouldDelegateToControllerDeleteProduct() {
        Product p1 = new Product("1", "Laptop", 999.99, "cat1");
        Product p2 = new Product("2", "Phone", 499.99, "cat2");
        GuiActionRunner.execute((GuiActionRunnable) () -> {
            DefaultListModel<Product> model = productCatalogSwingView.getListProductsModel();
            model.addElement(p1);
            model.addElement(p2);
        });
        window.list("productList").selectItem(1);
        window.button(JButtonMatcher.withText("Delete Selected")).click();
        verify(productCatalogController, timeout(TIMEOUT))
            .deleteProduct(p2);
    }
}
