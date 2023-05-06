open module com.example.peoplemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    exports com.example.peoplemanager;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
}