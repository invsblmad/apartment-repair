package project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.animations.Shake;
import project.database.Constants;
import project.database.DatabaseHandler;
import project.models.Apartment;
import project.models.Material;

public class DialogWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField amountField;

    @FXML
    private JFXComboBox<String> ownersBox;

    @FXML
    private ImageView closeIcon;

    @FXML
    private JFXButton saveButton;

    @FXML
    private Label wrongAmount;

    @FXML
    private Label duplicateMaterial;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    private TableView<Material> materialsTable;

    private Material material;

    private int totalAmount;

    private String ownersName;

    @FXML
    void initialize() {
        ObservableList<String> ownersNames = getOwnersNames(dbHandler.getApartments());
        ownersBox.setItems(ownersNames);

        saveButton.setOnAction(event -> {
            ownersName = ownersBox.getValue();
            String amount = amountField.getText();
            if (ownersName != null && amount != "") {
                addToOwnersTable(event, ownersName, Integer.parseInt(amount));
            }
        });

        closeIcon.setOnMouseClicked((MouseEvent event) -> {
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    private void addToOwnersTable(ActionEvent event, String ownersName, int amount) {
        String table = "owner_" + dbHandler.getOwnersId(ownersName);
        if (dbHandler.isMaterialDuplicated(table, material)) {
            showReactiveComment(duplicateMaterial);
        } else {
            if (isAmountPossible(amount)) {
                material.setAmount(amount);
                material.setId(dbHandler.getMaterialsId("materials", material));
                dbHandler.addMaterial(table, material);
                changeTotalAmount(amount);
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                shake(amountField);
                showReactiveComment(wrongAmount);
            }
        }
    }

    private boolean isAmountPossible(int amount) {
        if (((totalAmount - amount) < 0) || (amount <= 0)) {
            return false;
        } else {
            return true;
        }
    }

    private void changeTotalAmount(int amount) {
        dbHandler.setMaterialsAmount("materials", (totalAmount - amount), material);
        refreshTable();
    }

    private void refreshTable() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/project/layouts/materialsList.fxml"));
            fxmlLoader.load();

            MaterialsListController controller = fxmlLoader.getController();
            controller.setMaterialsTable(materialsTable, dbHandler.getMaterials(Constants.MATERIAL_TABLE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> getOwnersNames(List<Apartment> apartments) {
        ObservableList<String> owners = FXCollections.observableArrayList();
        for (int i = 0; i < apartments.size(); i++) {
            owners.add(apartments.get(i).getOwner());
        }
        return owners;
    }

    public void setApartmentsOwner(String owner) {
        ownersBox.getSelectionModel().select(owner);
    }

    public void setMaterialsTable(TableView<Material> materialsTable) {
        this.materialsTable = materialsTable;
    }

    public void setMaterial(Material material) {
        this.material = material;
        totalAmount = material.getAmount();
    }

    private void shake(Node node) {
        Shake nodeAnimation = new Shake(node);
        nodeAnimation.playAnimation();
    }

    private void showReactiveComment(Label text) {
        text.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
        visiblePause.setOnFinished(event -> {
            text.setVisible(false);
        });
        visiblePause.play();
    }

}
