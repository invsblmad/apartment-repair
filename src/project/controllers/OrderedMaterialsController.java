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

public class OrderedMaterialsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView backSign;

    @FXML
    private Label emptyList;

    @FXML
    private ImageView image;

    @FXML
    private VBox orderedMaterials;

    @FXML
    private ImageView orderedImage;

    @FXML
    private ScrollPane scroll;

    private ObservableList<Material> materials = FXCollections.observableArrayList();

    private DatabaseHandler dbHandler = new DatabaseHandler();

    @FXML
    void returnBack(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("ownersMaterials.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        setList();

        if (materials.size() > 0) {
            showPageContent();
            orderedMaterials.setSpacing(25);

            showMaterialsInList(materials);
        } else {
            hidePageContent();
        }

    }

    private void setList() {
        materials = dbHandler.getMaterialsByState(OwnersMaterialsController.table, "ordered");
    }

    private void showPageContent() {
        scroll.setVisible(true);
        orderedImage.setVisible(true);
        emptyList.setVisible(false);
        image.setVisible(false);
    }

    private void hidePageContent() {
        emptyList.setVisible(true);
        image.setVisible(true);
        scroll.setVisible(false);
        orderedImage.setVisible(false);
    }

    private void showMaterialsInList(ObservableList<Material> materials) {
        Node[] nodes = new Node[materials.size()];
        try {
            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/layouts/listItem.fxml"));
                nodes[i] = fxmlLoader.load();

                ListItemController controller = fxmlLoader.getController();
                controller.setMaterial(materials.get(i), i+1);

                orderedMaterials.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
