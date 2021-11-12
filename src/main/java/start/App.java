package start;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentationLayer.Employee;
import presentationLayer.MainMenu;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/presentationLayer/mainMenu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Client");
        primaryStage.setScene(new Scene(root, 1530, 800));
        primaryStage.show();

        MainMenu mainMenu = loader.getController();

        loader = new FXMLLoader(getClass().getResource("/presentationLayer/employee.fxml"));
        root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Employee");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

        Employee employee = loader.getController();
        mainMenu.getDeliveryService().addObserver(employee);
    }
}
