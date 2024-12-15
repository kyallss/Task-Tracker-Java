package org.example.tasktracker;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/task_tracker_db"; // Укажите свой URL базы данных
    private static final String USER = "postgres";
    private static final String PASSWORD = "asykpaeva";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the PostgreSQL database");
        }
    }

    public static void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS tasks (
                id SERIAL PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                description TEXT,
                due_date DATE,
                priority VARCHAR(50),
                status BOOLEAN
            );
        """;
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addTask(Task task) {
        String insertSQL = "INSERT INTO tasks (title, description, due_date, priority, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, Date.valueOf(task.getDueDate()));
            pstmt.setString(4, task.getPriority());
            pstmt.setBoolean(5, task.isStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteTask(Task task) {
        String deleteSQL = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getAllTasks() {
        String selectSQL = "SELECT * FROM tasks";
        try {
            Connection conn = connect();
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(selectSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void updateTask(Task task) {
        String updateSQL = "UPDATE tasks SET title=?, description=?, due_date=?, priority=?, status=? WHERE id=?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, Date.valueOf(task.getDueDate()));
            pstmt.setString(4, task.getPriority());
            pstmt.setBoolean(5, task.isStatus());
            pstmt.setInt(6, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
