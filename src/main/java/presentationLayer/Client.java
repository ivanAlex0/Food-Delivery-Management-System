package presentationLayer;

import model.MenuItem;
import model.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Client extends SceneController {
    @FXML
    TextField keywordField;
    @FXML
    TextField ratingField;
    @FXML
    TextField caloriesField;
    @FXML
    TextField proteinField;
    @FXML
    TextField fatsField;
    @FXML
    TextField sodiumField;
    @FXML
    TextField priceField;
    @FXML
    ListView<MenuItem> productListView;
    @FXML
    ListView<MenuItem> currentOrderListView;
    Order currentOrder;
    HashSet<MenuItem> currentOrderProducts = null;

    @FXML
    public void initialize() {
        deliveryService.products.clear();
        deliveryService.importProducts();
        ObservableList<MenuItem> productsObservableList = FXCollections.observableList(new ArrayList<>(deliveryService.products));
        productListView.setItems(productsObservableList);
        deliveryService.orders.clear();
        deliveryService.importOrders();
        System.out.println(deliveryService.orders);
    }

    /**
     * Function filters the products based on input criteria
     */
    @FXML
    public void filter() {
        List<MenuItem> filteredList = new ArrayList<>(deliveryService.products);
        if (!keywordField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(menuItem -> menuItem.getTitle().contains(keywordField.getText()))
                    .collect(Collectors.toList());
        }

        if (!ratingField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(menuItem -> menuItem.getRating() >= Float.parseFloat(ratingField.getText()))
                    .collect(Collectors.toList());
        }

        if (!caloriesField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(menuItem -> menuItem.getCalories() >= Float.parseFloat(caloriesField.getText()))
                    .collect(Collectors.toList());
        }

        if (!proteinField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(menuItem -> menuItem.getProtein() >= Float.parseFloat(proteinField.getText()))
                    .collect(Collectors.toList());
        }

        if (!fatsField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(menuItem -> menuItem.getFat() >= Float.parseFloat(fatsField.getText()))
                    .collect(Collectors.toList());
        }

        if (!sodiumField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(menuItem -> menuItem.getSodium() >= Float.parseFloat(sodiumField.getText()))
                    .collect(Collectors.toList());
        }

        if (!priceField.getText().isEmpty()) {
            filteredList = filteredList.stream()
                    .filter(menuItem -> menuItem.computePrice() >= Integer.parseInt(priceField.getText()))
                    .collect(Collectors.toList());
        }

        ObservableList<MenuItem> filteredObservableList = FXCollections.observableList(filteredList);
        productListView.setItems(filteredObservableList);
    }

    /**
     * Adds the current selected BaseProduct to the currentOrder
     */
    @FXML
    public void addToOrder() {
        if (currentOrderProducts == null)
            currentOrderProducts = new HashSet<>();
        MenuItem menuItem = productListView.getSelectionModel().getSelectedItem();
        if (menuItem != null) {
            currentOrderListView.getItems().add(menuItem);
            currentOrderProducts.add(menuItem);
        }
    }

    /**
     * Sends a request to DeliveryService to create an order
     */
    @FXML
    public void createOrder() {
        if (!currentOrderProducts.isEmpty()) {
            currentOrder = deliveryService.createOrder(user, currentOrderProducts);
            createBill();
            deliveryService.exportOrders();
        } else {
            showAlert("The basket is empty! Please select some products", "", "", Alert.AlertType.INFORMATION);
        }
    }

    /**
     * Shows a selected product in a new window
     */
    @FXML
    public void showProduct() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentationLayer/productInformation.fxml"));
        try {
            Parent root = loader.load();
            ProductInformation productInformation = loader.getController();
            productInformation.setMenuItem(productListView.getSelectionModel().getSelectedItem());
            Stage newWindow = new Stage();
            newWindow.setTitle("Second Stage");
            newWindow.setScene(new Scene(root, 800, 600));

            // Set position of second window, related to primary window.
            newWindow.setX(200);
            newWindow.setY(100);

            newWindow.show();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }


    }

    /**
     * Creates a bill for the current order
     */
    public void createBill() {
        String orderName = user.getUsername() + "_" + currentOrder.getOrderId() + "_" + new Date(System.currentTimeMillis()).toString() + ".txt";
        System.out.println(orderName);
        try {
            PrintWriter printWriter = new PrintWriter(orderName);
            printWriter.println("Order #" + currentOrder.getOrderId());
            printWriter.println("Client: " + user.getUsername());
            printWriter.println("Products: ");
            int totalPrice = 0;
            for (MenuItem currentOrderProduct : currentOrderProducts) {
                printWriter.println(currentOrderProduct);
                totalPrice += currentOrderProduct.computePrice();
            }
            printWriter.println("Price: " + totalPrice);
            printWriter.close();
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }
}
