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
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import project.Main;
import project.animations.Shake;
import project.database.DatabaseHandler;
import project.models.Material;

public class CartListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label emptyCart;

    @FXML
    private ImageView image;

    @FXML
    private AnchorPane payment;

    @FXML
    private PasswordField cardPassword;

    @FXML
    private Label deliveryPrice;

    @FXML
    private Label materialsPrice;

    @FXML
    private JFXButton payButton;

    @FXML
    private Label totalPrice;

    @FXML
    private ScrollPane scroll;

    @FXML
    private VBox orderedMaterials;

    private ObservableList<Material> cart = FXCollections.observableArrayList();

    private DatabaseHandler dbHandler = new DatabaseHandler();

    public static double ordersCost;
    public static double deliveryCost;
    public static double totalCost;

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
        setCart();

        if (cart.size() > 0) {
            showPageContent();
            orderedMaterials.setSpacing(25);

            showMaterialsInCart(cart);
            showOrdersCheck();
        } else {
            hidePageContent();
        }

        payButton.setOnAction(event -> {
            String name = dbHandler.getUsersName(OwnersHomeController.user);
            String pinCode = dbHandler.getOwnersPinCode(dbHandler.getOwnersId(name));

            if (!cardPassword.getText().equals(pinCode)) {
                shake(cardPassword);
            } else {
                DeliverItemController.ownersId = dbHandler.getOwnersId(name);
                DeliveredItemController.ownersId = dbHandler.getOwnersId(name);
                double cash = dbHandler.getOwnersCash(dbHandler.getOwnersId(name));
                if (cash >= totalCost) {
                    dbHandler.setOwnersCash(dbHandler.getOwnersId(name), Math.ceil(cash - totalCost));
                    clearCart();
                } else {
                    shake(payButton);
                }
            }
        });
    }

    private void clearCart() {
        for (int i = 0; i < cart.size(); i++) {
            dbHandler.setMaterialsState(OwnersMaterialsController.table, "ordered", cart.get(i));
        }
        try {
            Main main = new Main();
            main.changeScene("cartList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showOrdersCheck() {
        double order = 0;
        for (int i = 0; i < cart.size(); i++) {
            order += getEachTotalPrice(cart.get(i));
        }
        materialsPrice.setText(order + "$");

        double delivery;
        if (order >= 100.0) {
            delivery = 0.0;
        } else {
            delivery = Math.ceil(0.1 * order);
        }
        deliveryPrice.setText(delivery + "$");

        totalCost = order + delivery;
        totalPrice.setText(totalCost + "$");
    }

    private void showPageContent() {
        scroll.setVisible(true);
        payment.setVisible(true);
        emptyCart.setVisible(false);
        image.setVisible(false);
    }

    private void hidePageContent() {
        emptyCart.setVisible(true);
        image.setVisible(true);
        scroll.setVisible(false);
        payment.setVisible(false);
    }

    private void setCart() {
        cart = dbHandler.getMaterialsByState(OwnersMaterialsController.table, "cart");
    }

    private void showMaterialsInCart(ObservableList<Material> cart) {
        Node[] nodes = new Node[cart.size()];
        try {
            for (int i = 0; i < nodes.length; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/project/layouts/cartItem.fxml"));
                nodes[i] = fxmlLoader.load();

                CartIemController controller = fxmlLoader.getController();
                controller.setMaterial(cart.get(i));

                orderedMaterials.getChildren().add(nodes[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double getEachTotalPrice(Material material) {
        int amount = material.getAmount();
        double price = material.getPrice();
        return Math.ceil(amount * price);
    }

    private void shake(Node node) {
        Shake nodeAnimation = new Shake(node);
        nodeAnimation.playAnimation();
    }

}
