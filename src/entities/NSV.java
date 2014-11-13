package entities;

import controller.NSV_Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Casa on 12/11/2014.
 */
public class NSV {
    public Server show() {
        Stage s = new Stage();
        Parent root = null;
        NSV_Controller c;
        try {
            Server server = new Server("", "", 0, "");
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource("/view/newServerView.fxml"));
            root = myLoader.load();
            s.setScene(new Scene(root, 450, 240));
            s.setTitle("Nuevo Server");
            NSV_Controller controller = (NSV_Controller) myLoader.getController();
            controller.setServer(server);
            s.showAndWait();
            return server;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

