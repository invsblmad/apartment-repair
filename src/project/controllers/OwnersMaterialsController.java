package project.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import project.Main;
import project.animations.Shake;
import project.database.DatabaseHandler;
import project.listeners.MaterialListener;
import project.models.Material;

public class OwnersMaterialsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField amountField;

    @FXML
    private ImageView backSign;

    @FXML
    private JFXButton cartButton;

    @FXML
    private ImageView decreaseIcon;

    @FXML
    private HBox allMaterials;

    @FXML
    private HBox searchingResult;

    @FXML
    private GridPane grid;

    @FXML
    private GridPane grid1;

    @FXML
    private ImageView increaseIcon;

    @FXML
    private Label manufacturer;

    @FXML
    private ImageView materialsImage;

    @FXML
    private Label name;

    @FXML
    private Label price;

    @FXML
    private ImageView deliveredMaterials;

    @FXML
    private ImageView orderedMaterials;

    @FXML
    private ImageView home;

    @FXML
    private ImageView menu;

    @FXML
    private VBox menuButtons;

    @FXML
    private ImageView menuClose;

    @FXML
    private ImageView profile;

    @FXML
    private AnchorPane slider;

    @FXML
    private ImageView refreshIcon;

    @FXML
    private TextField searchField;

    @FXML
    private ScrollPane scroll;

    @FXML
    private ImageView searchIcon;

    @FXML
    private ScrollPane scroll1;

    @FXML
    private ImageView cartIcon;

    @FXML
    private Label unit;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    private ObservableList<Material> materials;

    private List<AnchorPane> foundMaterials;

    private MaterialListener listener;

    private Material newMaterial;

    private double oldPrice;

    private int oldAmount;

    public static String table;

    @FXML
    void addressToDeliveredList(MouseEvent event) {

    }

    @FXML
    void addressToHome(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("ownersHome.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addressToOrderedList(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("orderedMaterials.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addressToProfile(MouseEvent event) {

    }

    @FXML
    void returnBack(MouseEvent event) {
        try {
            Main main = new Main();
            main.changeScene("ownersHome.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        String name = dbHandler.getUsersName(OwnersHomeController.user);
        table = "owner_" + dbHandler.getOwnersId(name);
        materials = dbHandler.getMaterials(table);

        styleNodes(scroll, grid);
        styleNodes(scroll1, grid1);

        if (materials.size() > 0) {
            applyFunctions(table, materials.get(0));
            listener = new MaterialListener() {
                @Override
                public void onClickListener(Material material) {
                    applyFunctions(table, material);
                }
            };
        }
        showAllMaterials();
        searchMaterial();
        returnMaterials();

        menuButtons.setSpacing(20);
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

        cartIcon.setOnMouseClicked(mouseEvent -> {
            try {
                Main main = new Main();
                main.changeScene("cartList.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void applyFunctions(String table, Material material) {
        oldPrice = material.getPrice();
        oldAmount = material.getAmount();

        newMaterial = new Material(material.getId(), material.getName(), material.getManufacturer(),
                material.getPrice(), material.getUnit(), material.getAmount());

        setChosenMaterial(newMaterial);
        newMaterial.setId(dbHandler.getMaterialsId(table, material));

        increaseIcon.setOnMouseClicked(mouseEvent -> {
            increaseAmount(newMaterial);
        });

        decreaseIcon.setOnMouseClicked(mouseEvent -> {
            decreaseAmount(newMaterial);
        });

        addMaterialToCart(newMaterial);
    }

    private void setChosenMaterial(Material newMaterial) {
        name.setText(newMaterial.getName());
        price.setText((getTotalPrice(newMaterial)) + "$");
        manufacturer.setText(newMaterial.getManufacturer());
        unit.setText(newMaterial.getUnit());
        amountField.setText(String.valueOf(newMaterial.getAmount()));

        for (int i = 0; i < MaterialBoxController.images.length; i++) {
            if (newMaterial.getName().toLowerCase().equals(MaterialBoxController.images[i])) {
                newMaterial.setImage(MaterialBoxController.images[i] + ".png");
            }
        }
        Image image = new Image(getClass().getResourceAsStream("/project/materials/" + newMaterial.getImage()));
        materialsImage.setImage(image);
        correctImages(newMaterial);
    }

    private double getTotalPrice(Material newMaterial) {
        int amount = newMaterial.getAmount();
        double newPrice = Math.ceil(amount * oldPrice);
        newMaterial.setPrice(newPrice);
        return newPrice;
    }

    private void increaseAmount(Material newMaterial) {
        int newAmount = newMaterial.getAmount() + 1;
        newMaterial.setAmount(newAmount);
        amountField.setText(String.valueOf(newAmount));
        price.setText(getTotalPrice(newMaterial) + "$");
    }

    private void decreaseAmount(Material newMaterial) {
        int newAmount = newMaterial.getAmount() - 1;
        newMaterial.setAmount(newAmount);
        amountField.setText(String.valueOf(newAmount));
        price.setText(getTotalPrice(newMaterial) + "$");
    }

    private boolean isAmountPossible(int amount, Material material) {
        int totalAmount = dbHandler.getMaterialsAmount("materials", material);
        if (((totalAmount - amount) < 0)) {
            return false;
        } else {
            return true;
        }
    }

    private void changeTotalAmount(int amount, Material material) {
        int totalAmount = dbHandler.getMaterialsAmount("materials", material);
        dbHandler.setMaterialsAmount("materials", (totalAmount - amount), material);
    }

    private void addMaterialToCart(Material newMaterial) {
        cartButton.setOnAction(event -> {
            int newAmount = newMaterial.getAmount();
            if (newAmount == oldAmount) {
                dbHandler.setMaterialsState(table, "cart", newMaterial);
            } else {
                if ((isAmountPossible((newAmount - oldAmount), newMaterial) && newAmount > 0)) {
                    changeTotalAmount((newAmount - oldAmount), newMaterial);
                    dbHandler.setMaterialsAmount(table, newAmount, newMaterial);
                    dbHandler.setMaterialsState(table, "cart", newMaterial);
                } else {
                    shake(amountField);
                }
            }
        });
    }

    private void showAllMaterials() {
        int column = 0, row = 1;
        try {
            for (int i = 0; i < materials.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/layouts/materialBox.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                MaterialBoxController controller = fxmlLoader.getController();
                controller.setMaterial(materials.get(i), listener);

                if (column == 2) {
                    column = 0;
                    row++;
                }
                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(15));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void styleNodes(ScrollPane scroll, GridPane grid) {
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        grid.setHgap(35);
    }

    private void searchMaterial() {
        searchIcon.setOnMouseClicked(mouseEvent -> {
            allMaterials.setVisible(false);
            searchingResult.setVisible(true);
            foundMaterials = findMaterial(searchField.getText().toLowerCase());
        });
    }

    private void returnMaterials() {
        refreshIcon.setOnMouseClicked(mouseEvent -> {
            clearResult(foundMaterials);
            searchingResult.setVisible(false);
            allMaterials.setVisible(true);
        });
    }

    private List<AnchorPane> findMaterial(String parameter) {
        int column = 0, row = 1;
        List<AnchorPane> foundMaterials = new ArrayList<>();
        try {
            for (int i = 0; i < materials.size(); i++) {
                if (materials.get(i).getName().toLowerCase().equals(parameter) ||
                materials.get(i).getManufacturer().toLowerCase().equals(parameter)) {

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/project/layouts/materialBox.fxml"));

                    AnchorPane anchorPane = fxmlLoader.load();
                    foundMaterials.add(anchorPane);

                    if (column == 2) {
                        column = 0;
                        row++;
                    }
                    MaterialBoxController controller = fxmlLoader.getController();
                    controller.setMaterial(materials.get(i), listener);
                    grid1.add(anchorPane, column++, row);
                    GridPane.setMargin(anchorPane, new Insets(15));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundMaterials;
    }

    private void clearResult(List<AnchorPane> foundMaterials) {
        for (AnchorPane foundMaterial :foundMaterials) {
            grid1.getChildren().remove(foundMaterial);
        }
    }

    private void correctImages(Material material) {
        String image = material.getImage();
        if (image.contains("tile")) {
            setImageParameters(171, 148, 52, 29);
        } else if (image.contains("unknown")) {
            setImageParameters(113, 118, 75, 38);
        } else if (image.contains("brick")) {
            setImageParameters(194, 145, 41, 20);
        } else if (image.contains("adhesive")) {
            setImageParameters(161, 156, 55, 14);
        } else if (image.contains("laminate")) {
            setImageParameters(223, 150, 25, 20);
        } else if (image.contains("paint")) {
            setImageParameters(152, 171, 62, 14);
        } else if (image.contains("wallpaper")) {
            setImageParameters(194, 192, 43, -13);
        } else if (image.contains("drywall")) {
            setImageParameters(147, 132, 59, 33);
        } else {
            setImageParameters(175, 181, 44, 0);
        }
    }

    private void setImageParameters(int width, int height, int x, int y) {
        materialsImage.setFitWidth(width);
        materialsImage.setFitHeight(height);
        materialsImage.setLayoutX(x);
        materialsImage.setLayoutY(y);
    }

    private void shake(Node node) {
        Shake nodeAnimation = new Shake(node);
        nodeAnimation.playAnimation();
    }


}
