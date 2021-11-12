package presentationLayer;

import model.BaseProduct;
import model.CompositeProduct;
import model.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ProductInformation {

    @FXML
    ListView<MenuItem> productListView;

    public void setMenuItem(MenuItem menuItem) {
        if (menuItem instanceof BaseProduct) {
            System.out.println(((BaseProduct) menuItem).getTitle());
            productListView.getItems().add(menuItem);
        } else if (menuItem instanceof CompositeProduct) {
            ObservableList<MenuItem> productList = FXCollections.observableArrayList(((CompositeProduct) menuItem).products);
            productListView.setItems(productList);
        }
    }
}
