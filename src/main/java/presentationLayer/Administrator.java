package presentationLayer;

import model.BaseProduct;
import model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;

public class Administrator extends SceneController {

    @FXML
    TextField title;
    @FXML
    TextField ratingField;
    Float rating;
    @FXML
    TextField caloriesField;
    Float calories;
    @FXML
    TextField proteinField;
    Float protein;
    @FXML
    TextField fatField;
    Float fat;
    @FXML
    TextField sodiumField;
    Float sodium;
    @FXML
    TextField priceField;
    Integer price;
    @FXML
    ListView<MenuItem> productListView;
    BaseProduct currentBaseProduct = null;

    @FXML
    public void importProducts() {
        deliveryService.importProducts();
        ObservableList<MenuItem> productsObservableList = FXCollections.observableList(new ArrayList<>(deliveryService.products));
        productListView.setItems(productsObservableList);
    }

    @FXML
    public void writeProducts() {
        deliveryService.exportProducts();
    }

    @FXML
    public void addModifyProduct() {
        if (isProductValid()) {
            BaseProduct baseProduct = new BaseProduct(title.getText(), rating, calories, protein, fat, sodium, price);
            if (!deliveryService.addProduct(baseProduct)) {
                deliveryService.modifyProduct(baseProduct);
                showAlert("Item edited!", "It was edited!", "Fields missing!", Alert.AlertType.INFORMATION);
            }
        } else {
            showAlert("Some fields from product are missing", "Fields missing", "Fields missing!", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void deleteProduct() {
        if (currentBaseProduct == null) {
            //show alert
            showAlert("Product not selected!", "", "", Alert.AlertType.ERROR);
        } else {
            deliveryService.deleteProduct(currentBaseProduct);
        }
    }

    @FXML
    public void setBaseProduct() {
        currentBaseProduct = (BaseProduct) productListView.getSelectionModel().getSelectedItem();
        if (currentBaseProduct != null) {
            title.setText(currentBaseProduct.getTitle());
            ratingField.setText(currentBaseProduct.getRating().toString());
            caloriesField.setText(currentBaseProduct.getCalories().toString());
            proteinField.setText(currentBaseProduct.getProtein().toString());
            fatField.setText(currentBaseProduct.getFat().toString());
            sodiumField.setText(currentBaseProduct.getSodium().toString());
            priceField.setText(currentBaseProduct.getPrice().toString());
        }
    }

    @FXML
    public void goToComposite(ActionEvent event) {
        changeScene(event, "administratorCompositeProduct");
    }

    @FXML
    public void goToReports(ActionEvent event) {
        changeScene(event, "administratorReports");
    }

    ////verifications
    public boolean isProductValid() {
        try {
            rating = Float.parseFloat(ratingField.getText());
            calories = Float.parseFloat(caloriesField.getText());
            protein = Float.parseFloat(proteinField.getText());
            fat = Float.parseFloat(fatField.getText());
            sodium = Float.parseFloat(sodiumField.getText());
            price = Integer.parseInt(priceField.getText());
            return !title.getText().isEmpty();
        } catch (NumberFormatException numberFormatException) {
            numberFormatException.printStackTrace();
            return false;
        }
    }
}
