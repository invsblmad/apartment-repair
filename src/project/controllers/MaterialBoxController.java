package project.controllers;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import project.listeners.MaterialListener;
import project.models.Material;

public class MaterialBoxController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView materialsImage;

    @FXML
    private Label name;

    @FXML
    private Label unit;

    @FXML
    private Label manufacturer;

    @FXML
    private Label price;

    private Material material;

    private MaterialListener listener;

    public static String[] images = new String[]{"adhesive", "brick", "drywall", "laminate",
            "paint", "plinth", "primer", "tile", "wallpaper"};;

    @FXML
    private void click(MouseEvent event) {
        listener.onClickListener(material);
    }

    @FXML
    void initialize() {

    }

    public void setMaterial(Material material, MaterialListener listener) {
        this.material = material;
        name.setText(material.getName());
        price.setText(String.valueOf(material.getPrice()) + "$");
        manufacturer.setText(material.getManufacturer());
        unit.setText(material.getUnit());
        this.listener = listener;

        for (int i = 0; i < images.length; i++) {
            if (material.getName().toLowerCase().equals(images[i])) {
                material.setImage(images[i] + ".png");
            }
        }
        Image image = new Image(getClass().getResourceAsStream("/project/materials/" + material.getImage()));
        materialsImage.setImage(image);
        correctImages(material);
    }

    private void correctImages(Material material) {
        String image = material.getImage();
        if (image.contains("tile")) {
            setImageParameters(130, 133, 43, 23);
        } else if (image.contains("unknown")) {
            setImageParameters(134, 125, 37, 16);
        } else if (image.contains("brick")) {
            setImageParameters(152, 118, 32, 11);
        } else if (image.contains("laminate")) {
            setImageParameters(170, 120, 21, 13);
        } else if (image.contains("paint")) {
            setImageParameters(130, 133, 47, 7);
        } else if (image.contains("wallpaper")) {
            setImageParameters(163, 147, 38, -13);
        } else {
            setImageParameters(126, 123, 43, 7);
        }
    }

    private void setImageParameters(int width, int height, int x, int y) {
        materialsImage.setFitWidth(width);
        materialsImage.setFitHeight(height);
        materialsImage.setLayoutX(x);
        materialsImage.setLayoutY(y);
    }
}
