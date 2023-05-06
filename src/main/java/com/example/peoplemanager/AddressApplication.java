package com.example.peoplemanager;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddressApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddressApplication.class.getResource("people-overview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("People Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        /*StringProperty stringProperty = new SimpleStringProperty();
        stringProperty.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println(oldValue);
                System.out.println(newValue);
            }
        });

        stringProperty.setValue("Ciao");*/

    }
}