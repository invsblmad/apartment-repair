package project.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import project.models.Apartment;

public class ApartmentsInfoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label address;

    @FXML
    private Label area;

    @FXML
    private Label ownersName;

    @FXML
    private ImageView closeIcon;

    @FXML
    private Label repairType;

    private Apartment apartment;

    @FXML
    void initialize() {
        closeIcon.setOnMouseClicked(event -> {
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    public void setData(Apartment apartment) {
        this.apartment = apartment;
        ownersName.setText(apartment.getOwner());
        area.setText(String.valueOf(apartment.getArea()));
        address.setText(apartment.getAddress());
        repairType.setText(apartment.getRepairType());
    }


}
