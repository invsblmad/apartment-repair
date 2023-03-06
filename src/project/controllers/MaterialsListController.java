package project.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;
import project.Main;
import project.database.Constants;
import project.database.DatabaseHandler;
import project.models.Apartment;
import project.models.Material;

public class MaterialsListController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Material, Integer> amount;

    @FXML
    private JFXButton homeButton;

    @FXML
    private JFXButton logOutButton;

    @FXML
    private TableColumn<Material, String> manufacturer;

    @FXML
    private JFXButton materialsButton;

    @FXML
    private TableView<Material> materialsTable;

    @FXML
    private TableColumn<Material, String> name;

    @FXML
    private TableColumn<Material, Integer> number;

    @FXML
    private JFXButton ownersMaterialsButton;

    @FXML
    private TableColumn<Material, Double> price;

    @FXML
    private JFXButton profileButton;

    @FXML
    private TableColumn<Material, String> unit;

    @FXML
    private TableColumn<Material, String> images;

    @FXML
    private Label usersName;

    @FXML
    private AnchorPane addingWindow;

    @FXML
    private AnchorPane shadow;

    @FXML
    private TextField amountField;

    @FXML
    private ImageView closeIcon;

    @FXML
    private Label title;

    @FXML
    private TextField manufacturerField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField priceField;

    @FXML
    private JFXButton saveButton;

    @FXML
    private TextField unitField;

    @FXML
    private Label duplicateMaterial;

    @FXML
    private Label emptyField;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView infoIcon;

    @FXML
    private ImageView refreshIcon;

    @FXML
    private ToggleGroup filterGroup;

    @FXML
    private ImageView filterIcon;

    @FXML
    private AnchorPane filteringWindow;

    @FXML
    private JFXComboBox<String> ownersBox;

    @FXML
    private JFXRadioButton maxButton;

    @FXML
    private JFXRadioButton minButton;

    @FXML
    private ImageView backSign;

    @FXML
    private JFXButton applyButton;

    private DatabaseHandler dbHandler = new DatabaseHandler();

    private ObservableList<Material> materials;

    private List<Apartment> apartments;

    private Apartment apartment;

    private Material material;

    private boolean isUpdate;

    public static String apartmentsOwner;

    public static boolean isBackSignVisible;

    private ObservableList<Material> filteredMaterials = FXCollections.observableArrayList();

    public void setMaterialsTable(TableView<Material> materialsTable, ObservableList<Material> materials) {
        this.materialsTable = materialsTable;
        materialsTable.setItems(materials);
    }

    @FXML
    void refreshTable() {
        materials.clear();
        materials = dbHandler.getMaterials(Constants.MATERIAL_TABLE);
        materialsTable.setItems(materials);
    }

    @FXML
    void openAddingWindow() {
        title.setText("Add material");
        isUpdate = false;
        setTextFields("", "", "", "", "");
        addingWindow.setVisible(true);
        closeIcon.setOnMouseClicked((MouseEvent event) -> {
            addingWindow.setVisible(false);
        });
    }

    @FXML
    void openEditingWindow(MouseEvent event) {
        if (event.getClickCount() == 2) {
            material = materialsTable.getSelectionModel().getSelectedItem();
            isUpdate = true;
            title.setText("Edit material");
            setTextFields(material.getName(), material.getManufacturer(),
                    String.valueOf(material.getPrice()), material.getUnit(),
                    String.valueOf(material.getAmount()));
            addingWindow.setVisible(true);
            closeIcon.setOnMouseClicked(mouseEvent -> {
                addingWindow.setVisible(false);
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
    void initialize() {
        if (isBackSignVisible) {
            backSign.setVisible(true);
            backSign.setOnMouseClicked(mouseEvent -> {
                try {
                    openOwnersList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        usersName.setText(dbHandler.getUsersName(LogInPageController.user));
        createTableOfMaterials();
        searchMaterial();

        apartments = dbHandler.getApartments();
        ObservableList<String> names = getOwnersNames(apartments);
        ownersBox.setItems(names);

        if (apartmentsOwner != null) {
            ownersBox.getSelectionModel().select(apartmentsOwner);
        }

        infoIcon.setOnMouseClicked(event -> {
            String ownersName = ownersBox.getValue();
            if (ownersName != null) {
                apartment = getApartment(ownersName);
                openApartmentsInfo();
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

        ownersMaterialsButton.setOnAction(event -> {
            try {
                openOwnersList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String manufacturer = manufacturerField.getText();
            String price = priceField.getText();
            String unit = unitField.getText();
            String amount = amountField.getText();

            if (name.isEmpty() || manufacturer.isEmpty() || price.isEmpty()
                    || unit.isEmpty() || amount.isEmpty()) {
                showReactiveComment(emptyField);
            } else {
                handleData(name, manufacturer, price, unit, amount);
            }
        });
    }

    private void openOwnersList() throws IOException {
        try {
            String ownersName = ownersBox.getValue();
            OwnersListController.apartmentsOwner = ownersName;
            OwnersListController.isBackSignVisible = false;
            Main main = new Main();
            main.changeScene("ownersList.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private Apartment getApartment(String ownersName) {
        for (int i = 0; i < apartments.size(); i++) {
            if (apartments.get(i).getOwner().equals(ownersName)) {
                return apartments.get(i);
            }
        }
        return null;
    }

    private ObservableList<String> getOwnersNames(List<Apartment> apartments) {
        ObservableList<String> owners = FXCollections.observableArrayList();
        for (int i = 0; i < apartments.size(); i++) {
            owners.add(apartments.get(i).getOwner());
        }
        return owners;
    }

    private void handleData(String name, String manufacturer, String price, String unit, String amount) {
        Material newMaterial = new Material(name, manufacturer,
                Double.parseDouble(price), unit, Integer.parseInt(amount));
        if (isUpdate) {
            editMaterial(name, manufacturer, newMaterial);
        } else {
            addMaterial(newMaterial);
        }
    }

    private void editMaterial(String name, String manufacturer, Material newMaterial) {
        if (name.equals(material.getName()) && manufacturer.equals(material.getManufacturer())) {
            material.setId(dbHandler.getMaterialsId("materials", material));
            dbHandler.updateMaterial("materials", material.getId(), newMaterial);
            addingWindow.setVisible(false);
            refreshTable();
        } else {
            if (dbHandler.isMaterialDuplicated("materials", newMaterial)) {
                showReactiveComment(duplicateMaterial);
            } else {
                material.setId(dbHandler.getMaterialsId("materials", material));
                dbHandler.updateMaterial("materials", material.getId(), newMaterial);
                addingWindow.setVisible(false);
                refreshTable();
            }
        }
    }

    private void addMaterial(Material newMaterial) {
        if (dbHandler.isMaterialDuplicated("materials", newMaterial)) {
            showReactiveComment(duplicateMaterial);
        } else {
            dbHandler.addMaterial("materials", newMaterial);
            addingWindow.setVisible(false);
            refreshTable();
        }
    }

    private void createTableOfMaterials() {
        number.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        unit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        materials = dbHandler.getMaterials(Constants.MATERIAL_TABLE);
        materialsTable.setItems(materials);

        makeColumnOfImages();
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
                    HBox images = makeCellOfImages();
                    setGraphic(images);
                }
                setText(null);
                }
            };
            return cell;
        };
        images.setCellFactory(cellFactory);
        materialsTable.setItems(materials);
    }

    private HBox makeCellOfImages() {
        ImageView deleteIcon = createImage("/project/assets/trash.png");
        ImageView addIcon = createImage("/project/assets/cart.png");

        deleteIcon.setPickOnBounds(true);
        addIcon.setPickOnBounds(true);

        deleteIcon.setOnMouseClicked(event -> {
            deleteMaterial();
        });

        addIcon.setOnMouseClicked(mouseEvent -> {
            openDialogWindow();
        });

        HBox images = uniteImages(addIcon, deleteIcon);
        return images;
    }

    private ImageView createImage(String path) {
        ImageView image = new ImageView(path);
        image.setFitHeight(16);
        image.setFitWidth(16);
        return image;
    }

    private HBox uniteImages(ImageView image1, ImageView image2) {
        HBox images = new HBox(image1, image2);
        images.setStyle("-fx-alignment: baseline-left");
        HBox.setMargin(image2, new Insets(2, 2, 0, 3));
        HBox.setMargin(image1, new Insets(2, 3, 0, 2));
        return images;
    }

    private void deleteMaterial() {
        Material material = materialsTable.getSelectionModel().getSelectedItem();
        material.setId(dbHandler.getMaterialsId("materials", material));
        dbHandler.removeMaterial("materials", material);
        refreshTable();
    }

    private void openDialogWindow() {
        try {
            Material material = materialsTable.getSelectionModel().getSelectedItem();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/project/layouts/dialogWindow.fxml"));
            fxmlLoader.load();

            DialogWindowController controller = fxmlLoader.getController();
            controller.setMaterial(material);
            controller.setMaterialsTable(materialsTable);

            String ownersName = ownersBox.getValue();
            if (ownersName != null) {
                controller.setApartmentsOwner(ownersName);
            }
            Main main = new Main();
            main.openNewWindow(fxmlLoader, 560, 235);

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setApartmentsOwner(String owner) {
        ownersBox.getSelectionModel().select(owner);
    }
}
