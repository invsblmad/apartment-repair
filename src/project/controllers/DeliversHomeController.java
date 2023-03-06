package project.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.Main;
import project.database.DatabaseHandler;
import project.models.User;

public class DeliversHomeController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton deliveredMaterialsButton;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton logOutButton;

    @FXML
    private JFXButton materialsButton;

    @FXML
    private Label name;

    @FXML
    private JFXButton profileButton;

    @FXML
    private Label welcomeText;

    public static boolean isLoggedIn;

    public static User user;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void initialize() {

        if (isLoggedIn) {
            user = LogInPageController.user;
        } else {
            user = SignUpPageController.user;
        }

        String fullName = dbHandler.getUsersName(user);
        name.setText(fullName);
        welcomeText.setText("Welcome, " + fullName.split(" ")[0] + "!");

        logOutButton.setOnAction(event -> {
            try {
                Main main = new Main();
                main.changeScene("logInPage.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        materialsButton.setOnAction(event -> {
            try {
                Main main = new Main();
                main.changeScene("deliverList.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

}
