package org.example.tasktracker;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class TaskFormDialog {

    public static Task showTaskFormDialog(Task existingTask) {
        Dialog<Task> dialog = new Dialog<>();
        dialog.setTitle(existingTask == null ? "Add Task" : "Edit Task");
        dialog.setHeaderText("Enter Task Details");

        // Input Fields
        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");
        DatePicker dueDatePicker = new DatePicker();
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        CheckBox statusCheck = new CheckBox("Completed");

        // Prefill data if editing
        if (existingTask != null) {
            titleField.setText(existingTask.getTitle());
            descriptionField.setText(existingTask.getDescription());
            dueDatePicker.setValue(java.time.LocalDate.parse(existingTask.getDueDate()));
            priorityBox.setValue(existingTask.getPriority());
            statusCheck.setSelected(existingTask.isStatus());
        }

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Due Date:"), 0, 2);
        grid.add(dueDatePicker, 1, 2);
        grid.add(new Label("Priority:"), 0, 3);
        grid.add(priorityBox, 1, 3);
        grid.add(statusCheck, 1, 4);

        dialog.getDialogPane().setContent(grid);
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == saveButton) {
                return new Task(
                        existingTask != null ? existingTask.getId() : 0,
                        titleField.getText(),
                        descriptionField.getText(),
                        dueDatePicker.getValue() != null ? dueDatePicker.getValue().toString() : null,
                        priorityBox.getValue(),
                        statusCheck.isSelected()
                );
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }
}
