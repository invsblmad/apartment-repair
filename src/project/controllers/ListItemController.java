package project.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import project.database.DatabaseHandler;
import project.models.Material;

public class ListItemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField amountField;

    @FXML
    private Label manufacturer;

    @FXML
    private Label name;

    @FXML
    private Label number;

    @FXML
    private Label price;

    @FXML
    private Label state;

    @FXML
    private Label unit;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    private Material material;

    @FXML
    void initialize() {


    }

    public void setMaterial(Material material, int i) {
        this.material = material;
        number.setText(String.valueOf(i));
        name.setText(material.getName());
        manufacturer.setText(material.getManufacturer());
        amountField.setText(String.valueOf(material.getAmount()));
        price.setText(getTotalPrice(material) + "$");
        unit.setText(material.getUnit());
    }

    private double getTotalPrice(Material material) {
        int amount = material.getAmount();
        double price = material.getPrice();
        return Math.ceil(amount * price);
    }

}
