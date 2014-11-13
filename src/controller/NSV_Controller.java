package controller;

import entities.Server;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Casa on 12/11/2014.
 */
public class NSV_Controller implements Initializable {
    Server s;
    public
    @FXML
    TextField NSV_TF_SN;
    public
    @FXML
    TextField NSV_TF_IP;
    public
    @FXML
    TextField NSV_TF_Port;
    public
    @FXML
    TextField NSV_TF_UN;
    public
    @FXML
    PasswordField NSV_TF_PSS;
    public
    @FXML
    TextField NSV_TF_Service;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setServer(Server s) {
        this.s = s;
    }

    @FXML
    void OK() {
        //s = new Server(NSV_TF_SN.getText(),NSV_TF_IP.getText(),Integer.parseInt(NSV_TF_Port.getText()),NSV_TF_UN.getText());
        s.setIp(NSV_TF_IP.getText());
        s.setPass(NSV_TF_PSS.getText());
        s.setPort(Integer.parseInt(NSV_TF_Port.getText()));
        s.setServerName(NSV_TF_SN.getText());
        s.setUserName(NSV_TF_UN.getText());
        s.setService(NSV_TF_Service.getText());
        Stage stage = (Stage) NSV_TF_IP.getScene().getWindow();
        stage.close();
    }

    @FXML
    void Cancel() {
        Stage stage = (Stage) NSV_TF_IP.getScene().getWindow();
        stage.close();
    }
}
