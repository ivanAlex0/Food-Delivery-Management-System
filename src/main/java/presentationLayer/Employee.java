package presentationLayer;

import businessLayer.DeliveryService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.Observable;
import java.util.Observer;

public class Employee implements Observer {

    @FXML
    ListView<String> updateList;
    private DeliveryService deliveryService;

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("I got notified");
        deliveryService = (DeliveryService) o;
        updateList.getItems().add(deliveryService.getNotification());
        System.out.println(deliveryService.getNotification());
    }
}
