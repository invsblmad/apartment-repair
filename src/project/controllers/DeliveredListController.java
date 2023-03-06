package project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import project.Main;
import project.database.DatabaseHandler;
import project.models.Material;

public class DeliveredListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView backSign;

    @FXML
    private ImageView image;

    @FXML
    private VBox orderedMaterials;

    @FXML
    private ScrollPane scroll;

    @FXML
    private Label text;

    private ObservableList<Material> materials = FXCollections.observableArrayList();

    private DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void returnBack(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("deliverList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        setMaterials();
        if (materials.size() > 0) {
            orderedMaterials.setSpacing(25);
            showMaterialsInList(materials);
            showPageContent();
        } else {
            hidePageContent();
        }
    }

    private void showPageContent() {
        scroll.setVisible(true);
        text.setVisible(false);
        image.setVisible(false);
    }

    private void hidePageContent() {
        text.setVisible(true);
        image.setVisible(true);
        scroll.setVisible(false);
    }

    private void setMaterials() {
        materials = dbHandler.getMaterialsByState(OwnersMaterialsController.table, "delivered");
    }

    private void showMaterialsInList(ObservableList<Material> materials) {
        Node[] nodes = new Node[materials.size()];
        try {
            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/layouts/deliveredItem.fxml"));
                nodes[i] = fxmlLoader.load();

                DeliveredItemController controller = fxmlLoader.getController();
                controller.setMaterial(materials.get(i), i+1);

                orderedMaterials.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getTotalPrice(Material material) {
        int amount = material.getAmount();
        double price = material.getPrice();
        return Math.ceil(amount * price);
    }

}
