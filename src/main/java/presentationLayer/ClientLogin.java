package presentationLayer;

import model.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;

public class ClientLogin extends SceneController {

    @FXML
    TextField username;
    @FXML
    TextField password;

    @FXML
    public void initialize() {
        deliveryService.importUsers();
    }

    @FXML
    public void loginSignUp(ActionEvent actionEvent) {
        if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
            user = null;
            boolean isRegistered = false;
            User currentUser = new User(deliveryService.users.size() + 1, username.getText(), password.getText());
            for (User user1 : deliveryService.users) {
                if (user1.getUsername().equals(currentUser.getUsername()) && !user1.getPassword().equals(currentUser.getPassword())) {
                    showAlert("Password is incorrect!", "", "", Alert.AlertType.INFORMATION);
                    isRegistered = true;
                    break;
                }
                if (user1.equals(currentUser)) {
                    user = user1;
                    changeScene(actionEvent, "client");
                }
            }
            if (user == null && !isRegistered) {
                user = currentUser;
                deliveryService.users.add(user);
                writeUsers();
                changeScene(actionEvent, "client");
            }
        } else {
            showAlert("Some fields are missing", "Username or password missing!", " ", Alert.AlertType.ERROR);
        }
    }

    public void writeUsers() {
        CsvMapper mapper = new CsvMapper();
        mapper.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);

        CsvSchema schema = CsvSchema.builder().addColumn("Id").addColumn("Username").addColumn("Password").setUseHeader(true).build();

        ObjectWriter writer = mapper.writerFor(User.class).with(schema);

        File tempFile = new File("src/main/resources/clients.csv");

        try {
            writer.writeValues(tempFile).writeAll(deliveryService.users);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
