package project.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import project.Main;
import project.listeners.ApartmentListener;
import project.database.DatabaseHandler;
import project.models.Apartment;

public class ApartmentsListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private VBox menuButtons;

    @FXML
    private ImageView backSign;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ImageView menu;

    @FXML
    private ImageView menuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private ImageView home;

    @FXML
    private ImageView profile;

    @FXML
    private ImageView materials;

    @FXML
    private ImageView ownersMaterials;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    public static List<Apartment> apartments = new ArrayList<Apartment>();

    private ApartmentListener myListener;

    @FXML
    void returnBack(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("foremansHome.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addressToHome(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("foremansHome.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addressToMaterialsList(MouseEvent event) {
        try {
            MaterialsListController.isBackSignVisible = false;
            Main main = new Main();
            main.changeScene("materialsList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addressToOwnersList(MouseEvent event) {
        try {
            OwnersListController.isBackSignVisible = false;
            Main main = new Main();
            main.changeScene("ownersList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        menuButtons.setSpacing(20);

        apartments = dbHandler.getApartments();

        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        grid.setHgap(35);

        myListener = new ApartmentListener() {
            @Override
            public void onClickListener(Apartment apartment) {
                ApartmentController.apartment = apartment;
                try {
                    Main main = new Main();
                    main.changeScene("apartment.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        int column = 0, row = 1;
        try {
            for (int i = 0; i < apartments.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/layouts/apartmentBox.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ApartmentBoxController controller = fxmlLoader.getController();
                controller.setApartment(apartments.get(i), myListener);

                if (column == 3) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        slider.setTranslateX(99);

        menu.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(0);
            slide.play();

            slider.setTranslateX(99);

            slide.setOnFinished(actionEvent -> {
                menu.setVisible(false);
                menuClose.setVisible(true);
            });
        });

        menuClose.setOnMouseClicked(mouseEvent -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.4));
            slide.setNode(slider);

            slide.setToX(99);
            slide.play();

            slider.setTranslateX(0);

            slide.setOnFinished(actionEvent -> {
                menu.setVisible(true);
                menuClose.setVisible(false);
            });
        });
    }

}
