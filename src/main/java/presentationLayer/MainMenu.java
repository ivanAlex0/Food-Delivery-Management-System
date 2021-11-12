package presentationLayer;

import businessLayer.DeliveryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenu extends SceneController{

    @FXML
    public void goToAdministrator(ActionEvent event)
    {
        changeScene(event, "administrator");
    }

    @FXML
    public void goToClientLogin(ActionEvent event)
    {
        changeScene(event, "clientLogin");
    }

    public DeliveryService getDeliveryService()
    {
        return deliveryService;
    }
}
