package presentationLayer;

import model.CompositeProduct;
import model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class AdministratorComposite extends SceneController {

    @FXML
    TextField priceField;
    @FXML
    TextField title;
    @FXML
    public ListView<MenuItem> selectedProductsListView;
    @FXML
    public ListView<MenuItem> productListView;
    CompositeProduct compositeProduct = new CompositeProduct();

    @FXML
    public void initialize() {
        deliveryService.importProducts();
        ObservableList<MenuItem> productsObservableList = FXCollections.observableList(new ArrayList<>(deliveryService.products));
        productListView.setItems(productsObservableList);
    }

    @FXML
    public void addProduct() {
        MenuItem currentItem = productListView.getSelectionModel().getSelectedItem();
        if (currentItem != null) {
            System.out.println(currentItem);
            compositeProduct.getProducts().add(currentItem);
            priceField.setText(String.valueOf(compositeProduct.computePrice()));
            ObservableList<MenuItem> currentItems = FXCollections.observableArrayList(compositeProduct.getProducts());
            selectedProductsListView.setItems(currentItems);
        }
    }

    @FXML
    public void createComposite() {
        if (!compositeProduct.getProducts().isEmpty() && !title.getText().isEmpty()) {
            compositeProduct.setTitle(title.getText());
            deliveryService.createCustomProduct(compositeProduct);
        }
    }

    @FXML
    public void goToAdministrator(ActionEvent event) {
        changeScene(event, "administrator");
    }

}
