module org.example.tasktracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.tasktracker to javafx.fxml;
    exports org.example.tasktracker;
}