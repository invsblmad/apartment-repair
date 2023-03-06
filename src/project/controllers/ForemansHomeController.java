package project.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.Main;
import project.database.DatabaseHandler;

public class ForemansHomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton logOutButton;

    @FXML
    private JFXButton materialsButton;

    @FXML
    private JFXButton ownersMaterialsButton;

    @FXML
    private JFXButton profileButton;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton apartmentsButton;

    @FXML
    private Label welcomeText;

    @FXML
    private Label name;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void initialize() {
        String fullName = dbHandler.getUsersName(LogInPageController.user);
        name.setText(fullName);
        welcomeText.setText("Welcome, " + fullName.split(" ")[0] + "!");

        logOutButton.setOnAction(actionEvent -> {
            try {
                Main main = new Main();
                main.changeScene("logInPage.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        materialsButton.setOnAction(actionEvent -> {
            try {
                MaterialsListController.isBackSignVisible = false;
                Main main = new Main();
                main.changeScene("materialsList.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        ownersMaterialsButton.setOnAction(actionEvent -> {
            try {
                OwnersListController.isBackSignVisible = false;
                Main main = new Main();
                main.changeScene("ownersList.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        apartmentsButton.setOnAction(actionEvent -> {
            try {
                Main main = new Main();
                main.changeScene("apartmentsList.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
