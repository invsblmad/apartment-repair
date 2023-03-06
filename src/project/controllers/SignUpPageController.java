package project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import project.Main;
import project.animations.Shake;
import project.database.DatabaseHandler;
import project.models.User;

public class SignUpPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<String> accountTypeBox;

    @FXML
    private Label emptyAccountType;

    @FXML
    private Label emptyFirstName;

    @FXML
    private Label emptyLastName;

    @FXML
    private Label emptyPassword;

    @FXML
    private Label emptyUsername;

    @FXML
    private Label duplicateUser;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private JFXButton logInButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private TextField usernameField;

    ObservableList<String> accountTypes = FXCollections.observableArrayList("Owner", "Deliveryman", "Foreman");

    public static User user;

    @FXML
    void initialize() {
        User.isLoggedIn = false;

        accountTypeBox.setItems(accountTypes);

        logInButton.setOnAction(event -> {
            try {
                Main main = new Main();
                main.changeScene("logInPage.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        signUpButton.setOnAction(event -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = passwordField.getText();
            String accountType = (String) accountTypeBox.getValue();

            signUpUser(firstName, lastName, username, password, accountType);
        });

    }

    private void signUpUser(String firstName, String lastName, String username, String password, String accountType) {
        if (isUserDataFilled(firstName, lastName, username, password, accountType)) {
            saveUserData(firstName, lastName, username, password, accountType);
        } else {
            reactToEmptyData(firstName, lastName, username, password, accountType);
        }
    }

    private boolean isUserDataFilled(String firstName, String lastName, String username, String password, String accountType) {
        if (!firstName.equals("") && !lastName.equals("") &&
                !username.equals("") && !password.equals("") && accountType != null) {
            return true;
        } else {
            return false;
        }
    }

    private void reactToEmptyData(String firstName, String lastName, String username, String password, String accountType) {
        checkField(firstName, firstNameField, emptyFirstName);
        checkField(lastName, lastNameField, emptyLastName);
        checkField(username, usernameField, emptyUsername);
        checkField(password, passwordField, emptyPassword);
        checkComboBox(accountType, accountTypeBox, emptyAccountType);
    }

    private void saveUserData(String firstName, String lastName, String username, String password, String accountType) {
        user = new User(firstName, lastName, username, password, accountType);
        if (checkForDuplicateUser(user) >= 1) {
            showReactiveComment(duplicateUser);
        } else {
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.setUser(user);
            user.accessUser(user);
        }
    }

    private int checkForDuplicateUser(User user) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultSet = dbHandler.getUser(user);
        int counter = 0;
        try {
            while (resultSet.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counter;
    }

    private void checkField(String value, Node field, Label emptyField) {
        if (value.equals("")) {
            Shake nodeAnimation = new Shake(field);
            nodeAnimation.playAnimation();
            showReactiveComment(emptyField);
        }
    }

    private void checkComboBox(String value, Node box, Label emptyBox) {
        if (value == null) {
            Shake nodeAnimation = new Shake(box);
            nodeAnimation.playAnimation();
            showReactiveComment(emptyBox);
        }
    }

    private void showReactiveComment(Label text) {
        text.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
        visiblePause.setOnFinished(event -> {
            text.setVisible(false);
        });
        visiblePause.play();
    }

}
