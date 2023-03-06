package project.models;

import project.Main;
import project.controllers.DeliversHomeController;
import project.controllers.OwnersHomeController;

import java.io.IOException;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String accountType;

    public static boolean isLoggedIn;

    public User(String firstName, String lastName, String username, String password, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.accountType = accountType;
    }

    public User() {}

    public void accessUser(User user) {
        if (user.getAccountType().equals("Deliveryman")) {
            try {
                DeliversHomeController.isLoggedIn = isLoggedIn;
                Main main = new Main();
                main.changeScene("deliversHome.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (user.getAccountType().equals("Foreman")) {
            try {
                Main main = new Main();
                main.changeScene("foremansHome.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                OwnersHomeController.isLoggedIn = isLoggedIn;
                Main main = new Main();
                main.changeScene("ownersHome.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}

