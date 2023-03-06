package project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import project.Main;
import project.database.DatabaseHandler;
import project.models.Material;

public class CartIemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView materialsImage;

    @FXML
    private TextField amountField;

    @FXML
    private Label cancel;

    @FXML
    private Label manufacturer;

    @FXML
    private Label name;

    @FXML
    private Label price;

    @FXML
    private Label unit;

    private Material material;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void cancelOrder(MouseEvent event) {
        dbHandler.setMaterialsState(OwnersMaterialsController.table, "initial", material);
        try {
            Main main = new Main();
            main.changeScene("cartList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {

    }

    public void setMaterial(Material material) {
        this.material = material;
        name.setText(material.getName());
        manufacturer.setText(material.getManufacturer());
        amountField.setText(String.valueOf(material.getAmount()));
        price.setText(getTotalPrice(material) + "$");
        unit.setText(material.getUnit());

        for (int i = 0; i < MaterialBoxController.images.length; i++) {
            if (material.getName().toLowerCase().equals(MaterialBoxController.images[i])) {
                material.setImage(MaterialBoxController.images[i] + ".png");
            }
        }
        Image image = new Image(getClass().getResourceAsStream("/project/materials/" + material.getImage()));
        materialsImage.setImage(image);
        correctImages(material);
    }

    private double getTotalPrice(Material material) {
        int amount = material.getAmount();
        double price = material.getPrice();
        return Math.ceil(amount * price);
    }

    private void correctImages(Material material) {
        String image = material.getImage();
        if (image.contains("tile")) {
            setImageParameters(91, 78, 18, 21);
        } else if (image.contains("unknown")) {
            setImageParameters(84, 86, 15, 19);
        } else if (image.contains("brick")) {
            setImageParameters(111, 84, 11, 14);
        } else if (image.contains("laminate")) {
            setImageParameters(119, 87, 6, 17);
        } else if (image.contains("paint")) {
            setImageParameters(90, 98, 23, 6);
        } else if (image.contains("wallpaper")) {
            setImageParameters(108, 102, 14, 1);
        } else if (image.contains("adhesive")) {
            setImageParameters(104, 95, 14, 8);
        } else {
            setImageParameters(98, 110, 14, 8);
        }
    }

    private void setImageParameters(int width, int height, int x, int y) {
        materialsImage.setFitWidth(width);
        materialsImage.setFitHeight(height);
        materialsImage.setLayoutX(x);
        materialsImage.setLayoutY(y);
    }
}
