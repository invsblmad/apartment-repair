package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import project.Main;
import project.database.DatabaseHandler;
import project.models.Material;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeliveredItemController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label address;

    @FXML
    private TextField amountField;

    @FXML
    private Label deliver;

    @FXML
    private Label manufacturer;

    @FXML
    private Label name;

    @FXML
    private Label number;

    @FXML
    private Label price;

    @FXML
    private Label profit;

    @FXML
    private Label unit;

    private Material material;

    public static int ownersId;

    private DatabaseHandler dbHandler = new DatabaseHandler();

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

        double deliversProfit = Math.ceil(getTotalPrice(material) * 0.05);
        profit.setText(deliversProfit + "$");

        String apartmentsAddress = dbHandler.getApartmentsAddress(ownersId);
        address.setText(apartmentsAddress);
    }

    private double getTotalPrice(Material material) {
        int amount = material.getAmount();
        double price = material.getPrice();
        return Math.ceil(amount * price);
    }
}
