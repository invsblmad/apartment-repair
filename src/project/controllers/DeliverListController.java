package project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.Main;
import project.database.DatabaseHandler;
import project.models.Material;

public class DeliverListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView image;

    @FXML
    private Label income;

    @FXML
    private ImageView closeIcon;

    @FXML
    private JFXButton incomeButton;

    @FXML
    private AnchorPane incomeWindow;

    @FXML
    private Label text;

    @FXML
    private JFXButton deliveredMaterial;

    @FXML
    private ImageView backSign;

    @FXML
    private VBox orderedMaterials;

    @FXML
    private ScrollPane scroll;

    private ObservableList<Material> materials = FXCollections.observableArrayList();

    private DatabaseHandler dbHandler = new DatabaseHandler();

    private double deliversIncome;

    @FXML
    void returnBack(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("deliversHome.fxml");
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

            incomeButton.setOnAction(event -> {
                incomeWindow.setVisible(true);
                income.setText(countIncome(materials) + "$");
                closeIcon.setOnMouseClicked(mouseEvent -> {
                    incomeWindow.setVisible(false);
                });
            });

            deliveredMaterial.setOnAction(event -> {
                try {
                    Main main = new Main();
                    main.changeScene("deliveredList.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            hidePageContent();
            deliveredMaterial.setOnAction(event -> {
                try {
                    Main main = new Main();
                    main.changeScene("deliveredList.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
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
        materials = dbHandler.getMaterialsByState(OwnersMaterialsController.table, "ordered");
    }

    private void showMaterialsInList(ObservableList<Material> materials) {
        double sum = 0.0;
        Node[] nodes = new Node[materials.size()];
        try {
            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/layouts/deliverItem.fxml"));
                nodes[i] = fxmlLoader.load();

                DeliverItemController controller = fxmlLoader.getController();
                controller.setMaterial(materials.get(i), i+1);

                orderedMaterials.getChildren().add(nodes[i]);
                sum += Math.ceil(getTotalPrice(materials.get(i)) * 0.05);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        deliversIncome = sum;
    }

    private double countIncome(ObservableList<Material> materials) {
        double sum = 0.0;
        for (int i = 0; i < materials.size(); i++) {
            sum += Math.ceil(getTotalPrice(materials.get(i)) * 0.05);
        }
        return Math.ceil(sum);
    }

    private double getTotalPrice(Material material) {
        int amount = material.getAmount();
        double price = material.getPrice();
        return Math.ceil(amount * price);
    }
}
