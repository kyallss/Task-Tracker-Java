package org.example.tasktracker;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HelloController {

    @FXML
    private TableView<Task> taskTable;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> dueDateColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, Boolean> statusColumn;

    private final ObservableList<Task> tasks = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DatabaseConnection.initializeDatabase();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadTasksFromDatabase();
        taskTable.setItems(tasks);
    }

    private void loadTasksFromDatabase() {
        tasks.clear();
        try (ResultSet rs = DatabaseConnection.getAllTasks()) {
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getDate("due_date").toString(),
                        rs.getString("priority"),
                        rs.getBoolean("status")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onAddTask() {
        Task newTask = TaskFormDialog.showTaskFormDialog(null);
        if (newTask != null) {
            DatabaseConnection.addTask(newTask);
            loadTasksFromDatabase();
        }
    }

    @FXML
    private void onEditTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Task updatedTask = TaskFormDialog.showTaskFormDialog(selectedTask);
            if (updatedTask != null) {
                DatabaseConnection.updateTask(updatedTask);
                loadTasksFromDatabase();
            }
        } else {
            showAlert("No Task Selected", "Please select a task to edit.");
        }
    }

    @FXML
    private void onDeleteTask() {
        Task selectedTask = taskTable.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            DatabaseConnection.deleteTask(selectedTask);
            loadTasksFromDatabase();
        } else {
            showAlert("No Task Selected", "Please select a task to delete.");
        }
    }

    @FXML
    private void onExit() {
        Platform.exit();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
