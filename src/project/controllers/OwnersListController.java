package project.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import project.Main;
import project.animations.Shake;
import project.database.Constants;
import project.database.DatabaseHandler;
import project.models.Apartment;
import project.models.Material;

public class OwnersListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView addIcon;

    @FXML
    private TableColumn<Material, Integer> amount;

    @FXML
    private TextField amountField;

    @FXML
    private JFXButton applyButton;

    @FXML
    private ImageView closeIcon;

    @FXML
    private AnchorPane editingWindow;

    @FXML
    private Label emptyField;

    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private ImageView filterIcon;

    @FXML
    private AnchorPane filteringWindow;

    @FXML
    private JFXButton homeButton;

    @FXML
    private TableColumn<Material, String> images;

    @FXML
    private ImageView infoIcon;

    @FXML
    private JFXButton logOutButton;

    @FXML
    private TableColumn<Material, String> manufacturer;

    @FXML
    private TextField manufacturerField;

    @FXML
    private JFXButton materialsButton;

    @FXML
    private TableView<Material> materialsTable;

    @FXML
    private JFXRadioButton maxButton;

    @FXML
    private JFXRadioButton minButton;

    @FXML
    private TableColumn<Material, String> name;

    @FXML
    private TextField nameField;

    @FXML
    private TableColumn<Material, Integer> number;

    @FXML
    private JFXComboBox<String> ownersBox;

    @FXML
    private JFXButton ownersMaterialsButton;

    @FXML
    private TableColumn<Material, Double> price;

    @FXML
    private TextField priceField;

    @FXML
    private JFXButton profileButton;

    @FXML
    private ImageView refreshIcon;

    @FXML
    private JFXButton saveButton;

    @FXML
    private TextField searchField;

    @FXML
    private AnchorPane shadow;

    @FXML
    private Label title;

    @FXML
    private TableColumn<Material, String> unit;

    @FXML
    private TextField unitField;

    @FXML
    private Label wrongNumber;

    @FXML
    private Label usersName;

    @FXML
    private ImageView backSign;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    private ObservableList<Material> materials;

    private List<Apartment> apartments;

    private Apartment apartment;

    private Material material;

    public static String apartmentsOwner;

    private ObservableList<Material> filteredMaterials = FXCollections.observableArrayList();

    public static boolean isBackSignVisible;

    public static Apartment savedApartment;

    @FXML
    void openEditingWindow(MouseEvent event) {
        if (event.getClickCount() == 2) {
            material = materialsTable.getSelectionModel().getSelectedItem();
            title.setText("Edit material");
            setTextFields(material.getName(), material.getManufacturer(),
                    String.valueOf(material.getPrice()), material.getUnit(),
                    String.valueOf(material.getAmount()));
            editingWindow.setVisible(true);
            closeIcon.setOnMouseClicked(mouseEvent -> {
                editingWindow.setVisible(false);
            });
        }
    }

    private void setTextFields(String name, String manufacturer, String price, String unit, String amount) {
        nameField.setText(name);
        manufacturerField.setText(manufacturer);
        priceField.setText(price);
        unitField.setText(unit);
        amountField.setText(amount);
    }

    @FXML
    void openFilteringWindow(MouseEvent event) {
        filteringWindow.setVisible(true);
        applyButton.setOnAction(actionEvent -> {
            RadioButton selectedButton = (RadioButton) filterGroup.getSelectedToggle();
            filteringWindow.setVisible(false);
            applyFiltering(selectedButton.getText());
        });
    }

    private void applyFiltering(String selectedValue) {
        if (filteredMaterials != null) { filteredMaterials.clear();}
        Material filteredMaterial = null;
        if (selectedValue.contains("highest")) {
            filteredMaterial = getTheHighestPrice();
        } else {
            filteredMaterial = getTheLowestPrice();
        }
        filteredMaterials.add(filteredMaterial);
        materialsTable.setItems(filteredMaterials);
    }

    private Material getTheHighestPrice() {
        double maxPrice = materials.get(0).getPrice();
        Material filteredMaterial = materials.get(0);
        for (Material material : materials) {
            if (material.getPrice() > maxPrice) {
                maxPrice = material.getPrice();
                filteredMaterial = material;
            }
        }
        return filteredMaterial;
    }

    private Material getTheLowestPrice() {
        double minPrice = materials.get(0).getPrice();
        Material filteredMaterial = materials.get(0);
        for (Material material : materials) {
            if (material.getPrice() < minPrice) {
                minPrice = material.getPrice();
                filteredMaterial = material;
            }
        }
        return filteredMaterial;
    }

    @FXML
    void openMaterialsList(MouseEvent event) {
        try {
            MaterialsListController.apartmentsOwner = apartmentsOwner;
            MaterialsListController.isBackSignVisible = true;
            Main main = new Main();
            main.changeScene("materialsList.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void refreshTable() {
        materials.clear();
        materials = dbHandler.getMaterials(getTable(apartmentsOwner));
        materialsTable.setItems(materials);
    }

    @FXML
    void returnBack(MouseEvent event) {
        try {
            ApartmentController.apartment = savedApartment;
            Main main = new Main();
            main.changeScene("apartment.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        if (isBackSignVisible) {
            backSign.setVisible(true);
        }

        usersName.setText(dbHandler.getUsersName(LogInPageController.user));

        apartments = dbHandler.getApartments();
        ObservableList<String> names = getOwnersNames(apartments);
        ownersBox.setItems(names);
        ownersBox.getSelectionModel().select(apartmentsOwner);

        if (apartmentsOwner != null) {
            createTableOfMaterials(apartmentsOwner);
        }
        ownersBoxListener();


        materialsButton.setOnAction(event -> {
            try {
                MaterialsListController.isBackSignVisible = false;
                Main main = new Main();
                main.changeScene("materialsList.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        logOutButton.setOnAction(event -> {
            try {
                Main main = new Main();
                main.changeScene("logInPage.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        homeButton.setOnAction(event -> {
            try {
                Main main = new Main();
                main.changeScene("foremansHome.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        infoIcon.setOnMouseClicked(event -> {
            if (apartmentsOwner != null) {
                apartment = getApartment(apartmentsOwner);
                openApartmentsInfo();
            }
        });

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String manufacturer = manufacturerField.getText();
            String price = priceField.getText();
            String unit = unitField.getText();
            String amount = amountField.getText();

            if (price.isEmpty() || amount.isEmpty()) {
                showReactiveComment(emptyField);
            } else {
                editMaterial(name, manufacturer, Double.parseDouble(price), unit, Integer.parseInt(amount));
            }
        });
    }

    private void editMaterial(String name, String manufacturer, double price, String unit, int amount) {
        material.setId(dbHandler.getMaterialsId(getTable(apartmentsOwner), material));
        Material newMaterial = new Material(name, manufacturer, price, unit, amount);
        if (amount == material.getAmount()) {
            dbHandler.updateMaterial(getTable(apartmentsOwner), material.getId(), newMaterial);
            editingWindow.setVisible(false);
            refreshTable();
        } else {
            if (isAmountPossible(amount - material.getAmount()) && amount >= 0) {
                dbHandler.updateMaterial(getTable(apartmentsOwner), material.getId(), newMaterial);
                changeTotalAmount(amount - material.getAmount());
                editingWindow.setVisible(false);
                refreshTable();
            } else {
                shake(amountField);
                showReactiveComment(wrongNumber);
            }
        }
    }

    private boolean isAmountPossible(int amount) {
        int totalAmount = dbHandler.getMaterialsAmount("materials", material);
        if (((totalAmount - amount) < 0)) {
            return false;
        } else {
            return true;
        }
    }

    private void changeTotalAmount(int amount) {
        int totalAmount = dbHandler.getMaterialsAmount("materials", material);
        dbHandler.setMaterialsAmount("materials", (totalAmount - amount), material);
    }

     private void ownersBoxListener() {
        ownersBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            apartmentsOwner = newValue;
            createTableOfMaterials(apartmentsOwner);
        });
    }

    private void openApartmentsInfo() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/project/layouts/apartmentsInfo.fxml"));
            fxmlLoader.load();

            ApartmentsInfoController controller = fxmlLoader.getController();
            controller.setData(apartment);

            Main main = new Main();
            main.openNewWindow(fxmlLoader, 540, 235);
        } catch (Exception e) {
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

    private Apartment getApartment(String ownersName) {
        for (int i = 0; i < apartments.size(); i++) {
            if (apartments.get(i).getOwner().equals(ownersName)) {
                return apartments.get(i);
            }
        }
        return null;
    }

    private void createTableOfMaterials(String owner) {
        number.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        unit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        materials = dbHandler.getMaterials(getTable(owner));
        materialsTable.setItems(materials);

        makeColumnOfImages();
        searchMaterial();
    }

    private String getTable(String owner) {
        return "owner_" + dbHandler.getOwnersId(owner);
    }

    private void makeColumnOfImages() {
        Callback<TableColumn<Material, String>, TableCell<Material, String>> cellFactory = (TableColumn<Material, String> param) -> {
            final TableCell<Material, String> cell = new TableCell<Material, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(makeCellOfImage());
                    }
                    setText(null);
                }
            };
            return cell;
        };
        images.setCellFactory(cellFactory);
        materialsTable.setItems(materials);
    }

    private HBox makeCellOfImage() {
        ImageView deleteIcon = createImage("/project/assets/trash.png");
        deleteIcon.setPickOnBounds(true);

        deleteIcon.setOnMouseClicked(mouseEvent -> {
            deleteMaterial();
        });
        return createHBox(deleteIcon);
    }

    private ImageView createImage(String path) {
        ImageView image = new ImageView(path);
        image.setFitHeight(16);
        image.setFitWidth(16);
        return image;
    }

    private HBox createHBox(ImageView image) {
        HBox hBox = new HBox(image);
        hBox.setStyle("-fx-alignment: baseline-left");
        HBox.setMargin(image, new Insets(2, 3, 0, 2));
        return hBox;
    }

    private void deleteMaterial() {
        Material material = materialsTable.getSelectionModel().getSelectedItem();
        material.setId(dbHandler.getMaterialsId(getTable(apartmentsOwner), material));
        dbHandler.removeMaterial(getTable(apartmentsOwner), material);
        refreshTable();
    }

    private void searchMaterial() {
        FilteredList<Material> filteredData = new FilteredList<>(materials, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(material -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (material.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (material.getManufacturer().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Material> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(materialsTable.comparatorProperty());
        materialsTable.setItems(sortedData);
    }

    private void showReactiveComment(Label text) {
        text.setVisible(true);
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
        visiblePause.setOnFinished(event -> {
            text.setVisible(false);
        });
        visiblePause.play();
    }

    private void shake(Node node) {
        Shake nodeAnimation = new Shake(node);
        nodeAnimation.playAnimation();
    }

}
