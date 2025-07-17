package com.example.inventorysystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_TYPE = "jdbc:mysql";
    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "inventory_db";

    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "your_password";

    private static final String TBL_PRODUCTS = "products";

    private static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(DB_DRIVER);

            String url = DB_TYPE + "://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
            conn = DriverManager.getConnection(url, DB_USERNAME, DB_PASSWORD);
        } catch (ClassNotFoundException cnfex) {
            System.out.println("Failed to load MySQL JDBC driver.");
            System.out.println(cnfex.getMessage());
        } catch (SQLException sex) {
            System.out.println("Failed to connect to the database.");
            System.out.println("SQL Error Code: " + sex.getErrorCode());
            System.out.println(sex.getMessage());
        }
        return conn;
    }

    public static void initialize() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TBL_PRODUCTS + " ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(255) NOT NULL,"
                + "quantity INT NOT NULL,"
                + "price DECIMAL(10, 2) NOT NULL)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println("Error initializing database table: " + e.getMessage());
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM " + TBL_PRODUCTS;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
        return products;
    }

    public static void addProduct(String name, int quantity, double price) {
        String sql = "INSERT INTO " + TBL_PRODUCTS + "(name, quantity, price) VALUES(?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
        }
    }

    public static void updateProduct(int id, String name, int quantity, double price) {
        String sql = "UPDATE " + TBL_PRODUCTS + " SET name = ?, quantity = ?, price = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, quantity);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public static void deleteProduct(int id) {
        String sql = "DELETE FROM " + TBL_PRODUCTS + " WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }
}