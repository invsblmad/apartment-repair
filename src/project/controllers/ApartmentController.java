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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import project.Main;
import project.database.DatabaseHandler;
import project.models.Apartment;
import project.models.Material;

public class ApartmentController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton apartmentsButton;

    @FXML
    private Label ownersName;

    @FXML
    private Label repairType;

    @FXML
    private Label address;

    @FXML
    private Label area;

    @FXML
    private VBox materialsTable = null;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    private ObservableList<Material> materials = FXCollections.observableArrayList();

    public static Apartment apartment;

    @FXML
    void returnBack(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("apartmentsList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        setData(apartment);
        int id = dbHandler.getOwnersId(apartment.getOwner());
        materials = dbHandler.getMaterials("owner_" + id);

        apartmentsButton.setOnAction(event -> {
            try {
                openOwnersList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Node[] nodes = new Node[materials.size()];

        try {
            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/layouts/materialItem.fxml"));
                nodes[i] = fxmlLoader.load();

                MaterialItemController controller = fxmlLoader.getController();
                controller.setMaterial(materials.get(i), (i+1));

                final int j = i;

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color: #D3C6D6");
                });
                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color: #ffffff");
                });
                materialsTable.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }

    }

    private void openOwnersList() throws IOException {
        try {
            OwnersListController.apartmentsOwner = apartment.getOwner();
            OwnersListController.isBackSignVisible = true;
            OwnersListController.savedApartment = apartment;
            Main main = new Main();
            main.changeScene("ownersList.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(Apartment apartment) {
        ownersName.setText(apartment.getOwner());
        area.setText(String.valueOf(apartment.getArea()));
        address.setText(apartment.getAddress());
        repairType.setText(apartment.getRepairType());
    }

}
