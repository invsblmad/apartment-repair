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

public class LogInPageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXComboBox<String> accountTypeBox;

    @FXML
    private Label emptyAccountType;

    @FXML
    private Label emptyFields;

    @FXML
    private JFXButton logInButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private JFXButton signUpButton;

    @FXML
    private TextField usernameField;

    @FXML
    private Label wrongData;

    ObservableList<String> accountTypes = FXCollections.observableArrayList("Owner", "Deliveryman", "Foreman");

    public static User user;

    @FXML
    void initialize() {
        User.isLoggedIn = true;
        accountTypeBox.setItems(accountTypes);

        logInButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String accountType = accountTypeBox.getValue();

            logInUser(username, password, accountType);

        });

        signUpButton.setOnAction(event -> {
            try {
                Main main = new Main();
                main.changeScene("signUpPage.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void logInUser(String username, String password, String accountType) {
        if (isUserDataFilled(username, password, accountType)) {
            emptyFields.setText(null);
            emptyAccountType.setText(null);
            checkUserData(username, password, accountType);
        } else {
            reactToEmptyData(username, password);
        }
    }

    private boolean isUserDataFilled(String username, String password, String accountType) {
        if (!username.equals("") && !password.equals("") && accountType != null) {
            return true;
        } else {
            return false;
        }
    }

    private void reactToEmptyData(String username, String password) {
        if (username.equals("") || password.equals("")) {
            shake(usernameField);
            shake(passwordField);
            showReactiveComment(emptyFields);
        } else {
            shake(accountTypeBox);
            showReactiveComment(emptyAccountType);
        }
    }

    private void checkUserData(String username, String password, String accountType) {
        user = createUser(username, password, accountType);
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet resultSet = dbHandler.getUserForLogIn(user);
        if (countResultSet(resultSet) >= 1) {
            user.accessUser(user);
        } else {
            shake(usernameField);
            shake(passwordField);
            showReactiveComment(wrongData);
        }
    }

    private User createUser(String username, String password, String accountType){
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setAccountType(accountType);
        user.setId(dbHandler.getUsersId(user));
        return user;
    }

    private int countResultSet(ResultSet resultSet) {
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

    private void shake(Node node) {
        Shake nodeAnimation = new Shake(node);
        nodeAnimation.playAnimation();
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
