package com.example.peoplemanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

public class PersonOverviewController {
    @FXML private Label birthdayLabel;
    @FXML private Label cityLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label postalCodeLabel;
    @FXML private Label streetLabel;
    @FXML private TableColumn<Person, String> firstNameColumn;
    @FXML private TableColumn<Person, String> lastNameColumn;
    @FXML private TableView<Person> personTable;

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        personTable.setItems(getPersonData());

        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Person> getPersonData() {
        ObservableList<Person> persons = FXCollections.observableArrayList();
        persons.add(new Person("Hans", "Muster"));
        persons.add(new Person("Ruth", "Mueller"));
        persons.add(new Person("Heinz", "Kurz"));
        persons.add(new Person("Cornelia", "Meier"));
        persons.add(new Person("Werner", "Meyer"));
        persons.add(new Person("Lydia", "Kunz"));
        persons.add(new Person("Anna", "Best"));
        persons.add(new Person("Stefan", "Meier"));
        persons.add(new Person("Martin", "Mueller"));
        return persons;
    }

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(person.getBirthday().toString());
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    /**
     * Se l'indice è negativo lancia l'eccezione
     * Returns the index of the selected person in the TableView component
     * @return the index of the selected person
     */
    int selectedIndex() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            throw new NoSuchElementException();
        }
        return selectedIndex;
    }

    /**
     * Shows a simple warning dialog
     */
    void showNoPersonSelectedAlert() {
        /*
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Person Selected");
        alert.setContentText("Please select a person in the table.");
        alert.showAndWait();*/

        // Metodo equivalente ma meno configurabile
        new Alert(Alert.AlertType.WARNING, "No Person Selected").showAndWait();
    }


    /**
     * Al posto di calcolarsi l'indice dell'oggetto selezionato lo chiede ad una funzione
     * che può generare un eccezione
     * Se l'eccezione parte allora appare un Alert
     * */
    @FXML
    private void handleDeletePerson() {
        try {
            int selectedIndex = selectedIndex();
            personTable.getItems().remove(selectedIndex);
        } catch (NoSuchElementException e) {
            showNoPersonSelectedAlert();
        }
    }


    /**
     * Carico una nuova finestra instanzando un oggetto loader
     *
     */
    @FXML
    public void handleNewPerson() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("person-edit-view.fxml"));
            DialogPane view = loader.load();
            PersonEditDialogController controller = loader.getController();

            // Set an empty person into the controller
            controller.setPerson(new Person());

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Person");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                personTable.getItems().add(controller.getPerson());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpen() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                List<Person> persons = mapper.readValue(file, new TypeReference<List<Person>>() {});
                personTable.getItems().addAll(persons);
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not load data").showAndWait();
        }
    }

    @FXML
    private void handleSaveAs() {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, personTable.getItems());
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Could not save data").showAndWait();
        }
    }

        @FXML
    private void handleNew() {
        personTable.getItems().clear();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Address Application");
        alert.setHeaderText("About");
        alert.setContentText("Author: nbicocchi@unimore.it");
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}