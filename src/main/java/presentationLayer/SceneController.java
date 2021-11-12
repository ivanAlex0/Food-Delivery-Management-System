package presentationLayer;

import businessLayer.DeliveryService;
import model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    public static DeliveryService deliveryService = null;
    public Scene scene = null;
    public Stage stage = null;
    public static User user = null;

    public SceneController() {
        if (deliveryService == null) {
            deliveryService = new DeliveryService();
        }
    }

    public void showAlert(String title, String header, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    @FXML
    public void changeScene(ActionEvent actionEvent, String sceneName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentationLayer/" + sceneName + ".fxml"));
        try {
            Parent root = loader.load();
            setSceneAndStage(root, actionEvent);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void setSceneAndStage(Parent root, ActionEvent actionEvent) {
        if (scene == null) {
            scene = new Scene(root);
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } else
            stage.getScene().setRoot(root);
    }
}
