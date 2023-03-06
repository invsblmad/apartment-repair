package project.controllers;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import project.listeners.ApartmentListener;
import project.models.Apartment;

public class ApartmentBoxController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label apartmentNumber;

    @FXML
    private JFXButton materialsButton;

    private Apartment apartment;

    private ApartmentListener myListener;

    @FXML
    void initialize() {
        materialsButton.setOnAction(event -> {
            myListener.onClickListener(apartment);
        });
    }

    public void setApartment(Apartment apartment, ApartmentListener myListener) {
        this.apartment = apartment;
        this.myListener = myListener;
        apartmentNumber.setText(String.valueOf(apartment.getNumber()));

    }
}
