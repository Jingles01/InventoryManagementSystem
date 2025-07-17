package com.example.inventorysystem;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private final TableView<Product> table = new TableView<>();
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    private final TextField nameInput = new TextField();
    private final TextField quantityInput = new TextField();
    private final TextField priceInput = new TextField();

    public static void main(String[] args) {
        Database.initialize();
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Inventory Management System");

        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(50);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product, Double> priceColumn = getColumn();
        loadData();
        table.setItems(productList);
        table.getColumns().addAll(idColumn, nameColumn, quantityColumn, priceColumn);

        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(10, 10, 10, 10));
        formPane.setVgap(8);
        formPane.setHgap(10);

        Label nameLabel = new Label("Name:");
        Label quantityLabel = new Label("Quantity:");
        Label priceLabel = new Label("Price:");

        nameInput.setPromptText("Product name");
        quantityInput.setPromptText("e.g., 100");
        priceInput.setPromptText("e.g., 19.99");

        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setConstraints(nameInput, 1, 0);
        GridPane.setConstraints(quantityLabel, 0, 1);
        GridPane.setConstraints(quantityInput, 1, 1);
        GridPane.setConstraints(priceLabel, 0, 2);
        GridPane.setConstraints(priceInput, 1, 2);

        formPane.getChildren().addAll(
                nameLabel, nameInput,
                quantityLabel, quantityInput,
                priceLabel, priceInput
        );


        Button addButton = new Button("Add Product");
        addButton.setOnAction(e -> addButtonClicked());

        Button updateButton = new Button("Update Product");
        updateButton.setOnAction(e -> updateButtonClicked());

        Button deleteButton = new Button("Delete Product");
        deleteButton.setOnAction(e -> deleteButtonClicked());

        HBox buttonPane = new HBox(10);
        buttonPane.setPadding(new Insets(10, 10, 10, 10));
        buttonPane.getChildren().addAll(addButton, updateButton, deleteButton);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameInput.setText(newSelection.getName());
                quantityInput.setText(String.valueOf(newSelection.getQuantity()));
                priceInput.setText(String.format("%.2f", newSelection.getPrice()));
            }
        });


        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(table);
        mainLayout.setBottom(formPane);

        VBox bottomContainer = new VBox(10);
        bottomContainer.getChildren().addAll(formPane, buttonPane);
        mainLayout.setBottom(bottomContainer);

        Scene scene = new Scene(mainLayout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private static TableColumn<Product, Double> getColumn() {
        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setCellFactory(tc -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });
        return priceColumn;
    }

    private void loadData() {
        productList.clear();
        productList.addAll(Database.getAllProducts());
    }

    private void addButtonClicked() {
        try {
            String name = nameInput.getText();
            int quantity = Integer.parseInt(quantityInput.getText());
            double price = Double.parseDouble(priceInput.getText());

            if(name.isEmpty()) {
                showAlert("Form Error", "Name field cannot be empty.");
                return;
            }

            Database.addProduct(name, quantity, price);
            clearInputFields();
            loadData();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for Quantity and Price.");
        }
    }

    private void updateButtonClicked() {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Selection Error", "Please select a product to update.");
            return;
        }

        try {
            int id = selectedProduct.getId();
            String name = nameInput.getText();
            int quantity = Integer.parseInt(quantityInput.getText());
            double price = Double.parseDouble(priceInput.getText());

            Database.updateProduct(id, name, quantity, price);
            clearInputFields();
            loadData();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for Quantity and Price.");
        }
    }

    private void deleteButtonClicked() {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert("Selection Error", "Please select a product to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Product: " + selectedProduct.getName());
        alert.setContentText("Are you sure you want to delete this product? This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Database.deleteProduct(selectedProduct.getId());
                clearInputFields();
                loadData();
            }
        });
    }

    private void clearInputFields() {
        nameInput.clear();
        quantityInput.clear();
        priceInput.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}