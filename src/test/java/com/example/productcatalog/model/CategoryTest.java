package com.example.productcatalog.model;

import static org.assertj.core.api.Assertions.*;
import org.junit.Test;

public class CategoryTest {

    @Test
    public void testConstructorAndGetters() {
        Category c = new Category("1", "Electronics");
        assertThat(c.getId()).isEqualTo("1");
        assertThat(c.getName()).isEqualTo("Electronics");
    }

    @Test
    public void testDefaultConstructor() {
        Category c = new Category();
        assertThat(c.getId()).isNull();
        assertThat(c.getName()).isNull();
    }

    @Test
    public void testSetters() {
        Category c = new Category();
        c.setId("1");
        c.setName("Electronics");
        assertThat(c.getId()).isEqualTo("1");
        assertThat(c.getName()).isEqualTo("Electronics");
    }

    @Test
    public void testEqualsSameObject() {
        Category c = new Category("1", "Electronics");
        assertThat(c).isEqualTo(c);
    }

    @Test
    public void testEqualsSameId() {
        Category c1 = new Category("1", "Electronics");
        Category c2 = new Category("1", "Computers");
        assertThat(c1).isEqualTo(c2);
    }

    @Test
    public void testNotEqualsDifferentId() {
        Category c1 = new Category("1", "Electronics");
        Category c2 = new Category("2", "Electronics");
        assertThat(c1).isNotEqualTo(c2);
    }

    @Test
    public void testNotEqualsNull() {
        Category c = new Category("1", "Electronics");
        assertThat(c).isNotEqualTo(null);
    }

    @Test
    public void testNotEqualsDifferentClass() {
        Category c = new Category("1", "Electronics");
        assertThat(c).isNotEqualTo("a string");
    }

    @Test
    public void testHashCode() {
        Category c1 = new Category("1", "Electronics");
        Category c2 = new Category("1", "Computers");
        assertThat(c1).hasSameHashCodeAs(c2);
    }

    @Test
    public void testToString() {
        Category c = new Category("1", "Electronics");
        assertThat(c.toString()).contains("1", "Electronics");
    }
}
