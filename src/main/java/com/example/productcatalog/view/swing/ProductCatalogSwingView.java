package com.example.productcatalog.view.swing;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.example.productcatalog.model.Product;
import com.example.productcatalog.controller.ProductCatalogController;
import com.example.productcatalog.view.ProductCatalogView;

public class ProductCatalogSwingView extends JFrame implements ProductCatalogView {

    private static final long serialVersionUID = 1L;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtCategoryId;
    private JButton btnAdd;
    private JButton btnDeleteSelected;
    private JList<Product> listProducts;
    private DefaultListModel<Product> listProductsModel;
    private JLabel lblErrorMessage;

    private transient ProductCatalogController productCatalogController;

    public ProductCatalogSwingView() {
        setTitle("Product Catalog");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtId = new JTextField(15);
        txtId.setName("idTextBox");
        inputPanel.add(txtId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtName = new JTextField(15);
        txtName.setName("nameTextBox");
        inputPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtPrice = new JTextField(15);
        txtPrice.setName("priceTextBox");
        inputPanel.add(txtPrice, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Category ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        txtCategoryId = new JTextField(15);
        txtCategoryId.setName("categoryIdTextBox");
        inputPanel.add(txtCategoryId, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        btnAdd = new JButton("Add");
        btnAdd.setName("addButton");
        btnAdd.setEnabled(false);
        inputPanel.add(btnAdd, gbc);

        contentPane.add(inputPanel, BorderLayout.NORTH);

        listProductsModel = new DefaultListModel<>();
        listProducts = new JList<>(listProductsModel);
        listProducts.setName("productList");
        listProducts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listProducts);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        btnDeleteSelected = new JButton("Delete Selected");
        btnDeleteSelected.setName("deleteButton");
        btnDeleteSelected.setEnabled(false);
        bottomPanel.add(btnDeleteSelected, BorderLayout.NORTH);

        lblErrorMessage = new JLabel(" ");
        lblErrorMessage.setName("errorMessageLabel");
        bottomPanel.add(lblErrorMessage, BorderLayout.SOUTH);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        addListeners();
    }

    private void addListeners() {
        KeyAdapter btnAddEnabler = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                btnAdd.setEnabled(
                    !txtId.getText().trim().isEmpty() &&
                    !txtName.getText().trim().isEmpty() &&
                    !txtPrice.getText().trim().isEmpty() &&
                    !txtCategoryId.getText().trim().isEmpty()
                );
            }
        };
        txtId.addKeyListener(btnAddEnabler);
        txtName.addKeyListener(btnAddEnabler);
        txtPrice.addKeyListener(btnAddEnabler);
        txtCategoryId.addKeyListener(btnAddEnabler);

        btnAdd.addActionListener(e -> {
            Product product = new Product(
                txtId.getText().trim(),
                txtName.getText().trim(),
                Double.parseDouble(txtPrice.getText().trim()),
                txtCategoryId.getText().trim()
            );
            productCatalogController.newProduct(product);
        });

        listProducts.addListSelectionListener(e ->
            btnDeleteSelected.setEnabled(listProducts.getSelectedIndex() != -1)
        );

        btnDeleteSelected.addActionListener(e -> {
            Product selectedProduct = listProducts.getSelectedValue();
            if (selectedProduct != null) {
                productCatalogController.deleteProduct(selectedProduct);
            }
        });
    }

    public void setProductCatalogController(ProductCatalogController controller) {
        this.productCatalogController = controller;
    }

    DefaultListModel<Product> getListProductsModel() {
        return listProductsModel;
    }

    @Override
    public void showAllProducts(java.util.List<Product> products) {
        listProductsModel.clear();
        products.forEach(listProductsModel::addElement);
    }

    @Override
    public void showError(String message, Product product) {
        lblErrorMessage.setText(message + ": " + product);
    }

    @Override
    public void productAdded(Product product) {
        listProductsModel.addElement(product);
        lblErrorMessage.setText(" ");
    }

    @Override
    public void productRemoved(Product product) {
        listProductsModel.removeElement(product);
        lblErrorMessage.setText(" ");
    }
}