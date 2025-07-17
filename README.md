# Inventory Management System

A database-driven desktop application for managing product inventory, built with JavaFX and MySQL.

**Link to Video Demo:** [https://www.youtube.com/watch?v=5_5dNkJDp24](https://www.youtube.com/watch?v=5_5dNkJDp24)


## How It's Made:

**Tech used:** Java, JavaFX, MySQL, Maven

This application was built from the ground up as a real-world example of a Java desktop application. It follows a classic 3-tier architecture, separating the UI (built programmatically with **JavaFX**) from the core application logic and the data access layer. A dedicated `Database.java` class manages all CRUD operations with the **MySQL** server. Project dependencies are handled by **Maven**.

## Optimizations

- **Search and Filter:** Implement a search bar to quickly find products by name or ID.
- **Reporting:** Add a feature to generate and export inventory reports.
- **Low-Stock Alerts:** Automatically highlight products in the UI when their quantity falls below a certain threshold.
- **User Authentication:** Add a login system to differentiate between user roles.

## Lessons Learned:

- **The Importance of Separation of Concerns:** Creating a dedicated `Database` class instead of mixing SQL queries with UI code in `Main.java` made the project significantly easier to debug, maintain and understand.
- **Data Binding with JavaFX Properties:** Using JavaFX's `SimpleStringProperty`, `SimpleIntegerProperty`, etc in the `Product` class was a powerful lesson. It allows the `TableView` to automatically update when the underlying data changes.
- **Dependency Management with Maven:** Understanding how to use a `pom.xml` file to manage project dependencies like the MySQL driver and to resolve security vulnerabilities is a critical skill for any professional Java developer.
- **Database Integration Challenges:** Successfully connecting a Java application to an external MySQL server, handling credentials securely and troubleshooting connection errors provided practical, real-world experience.
