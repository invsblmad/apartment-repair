package project.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.models.Material;

public class MaterialItemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label amount;

    @FXML
    private Label manufacturer;

    @FXML
    private Label name;

    @FXML
    private Label number;

    private Material material;

    @FXML
    void initialize() {

    }

    public void setMaterial(Material material, int i) {
        this.material = material;
        number.setText(String.valueOf(i));
        name.setText(material.getName());
        manufacturer.setText(material.getManufacturer());
        amount.setText(String.valueOf(material.getAmount()));
    }

}
