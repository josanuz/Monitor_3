package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Casa on 11/11/2014.
 */
public class CNT_Controller implements Initializable, ControlledScreen {
    NewTaskDialogController tvController;
    @Override
    public void setScreenParent(NewTaskDialogController screenPage) {
        tvController = screenPage;
    }

    @Override
    public void clearData() {
    }

    @Override
    public void reloadMainData() {
    }

    @Override
    public void close() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    void OK() {
        tvController.createTask();
        tvController.close();
    }

    @FXML
    void Back() {
        tvController.previousScreen();
    }

    @FXML
    void Cancel() {
        tvController.close();
    }
    /**Added Comment**/
}
