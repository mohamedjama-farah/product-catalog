package com.example.productcatalog.model;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class ProductTest {

    @Test
    public void testConstructorAndGetters() {
        Product p = new Product("1", "Laptop", 999.99, "cat1");
        assertThat(p.getId()).isEqualTo("1");
        assertThat(p.getName()).isEqualTo("Laptop");
        assertThat(p.getPrice()).isEqualTo(999.99);
        assertThat(p.getCategoryId()).isEqualTo("cat1");
    }

    @Test
    public void testDefaultConstructor() {
        Product p = new Product();
        assertThat(p.getId()).isNull();
        assertThat(p.getName()).isNull();
    }

    @Test
    public void testSetters() {
        Product p = new Product();
        p.setId("1");
        p.setName("Laptop");
        p.setPrice(999.99);
        p.setCategoryId("cat1");
        assertThat(p.getId()).isEqualTo("1");
        assertThat(p.getName()).isEqualTo("Laptop");
        assertThat(p.getPrice()).isEqualTo(999.99);
        assertThat(p.getCategoryId()).isEqualTo("cat1");
    }

    @Test
    public void testEqualsSameObject() {
        Product p = new Product("1", "Laptop", 999.99, "cat1");
        assertThat(p).isEqualTo(p);
    }

    @Test
    public void testEqualsSameId() {
        Product p1 = new Product("1", "Laptop", 999.99, "cat1");
        Product p2 = new Product("1", "Phone", 499.99, "cat2");
        assertThat(p1).isEqualTo(p2);
    }

    @Test
    public void testNotEqualsDifferentId() {
        Product p1 = new Product("1", "Laptop", 999.99, "cat1");
        Product p2 = new Product("2", "Laptop", 999.99, "cat1");
        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    public void testNotEqualsNull() {
        Product p = new Product("1", "Laptop", 999.99, "cat1");
        assertThat(p).isNotEqualTo(null);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        Product p = new Product("1", "Laptop", 999.99, "cat1");
        assertThat(p).isNotEqualTo("a string");
    }

    @Test
    public void testHashCode() {
        Product p1 = new Product("1", "Laptop", 999.99, "cat1");
        Product p2 = new Product("1", "Phone", 499.99, "cat2");
        assertThat(p1).hasSameHashCodeAs(p2);
    }

    @Test
    public void testToString() {
        Product p = new Product("1", "Laptop", 999.99, "cat1");
        assertThat(p.toString()).contains("1", "Laptop", "999.99");
    }
}
