package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Created by Casa on 11/11/2014.
 */
public class NTV_Controller implements Initializable, ControlledScreen {
    NewTaskDialogController taskViewController;
    @FXML
    TextField NTV_TF_Name;
    @FXML
    ComboBox<String> NTV_CMB_Type;
    @FXML
    ChoiceBox<Integer> NTV_CB_Level;
    @FXML
    Label NTV_LBL_Level;
    ObservableList<String> TaskOptions = FXCollections.observableArrayList(Arrays.asList("Completo", "Parcial", "Incremental", "Lógico"));
    ObservableList<Integer> levelOptions = FXCollections.observableArrayList();
    private int selectedType = 0, selectedLevel = 0;
    Function<String, Integer> Print = (String s) -> {
        System.out.print(s);
        return 0;
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (int i = 0; i < 11; i++) levelOptions.add(i);
        NTV_CB_Level.setItems(levelOptions);
        NTV_CMB_Type.setItems(TaskOptions);
        NTV_CB_Level.getSelectionModel().select(0);
        NTV_CMB_Type.getSelectionModel().select(0);
        NTV_CB_Level.getSelectionModel().selectedIndexProperty().addListener((source, old, nw) -> selectedLevel = nw.intValue());
        NTV_CMB_Type.getSelectionModel().selectedIndexProperty().addListener((source, old, nw) -> {
            selectedType = nw.intValue();
            if (selectedType == 2) {
                NTV_LBL_Level.setVisible(true);
                NTV_CB_Level.setVisible(true);
            } else {
                NTV_LBL_Level.setVisible(false);
                NTV_CB_Level.setVisible(false);
            }
        });
    }

    @Override
    public void setScreenParent(NewTaskDialogController screenPage) {
        taskViewController = screenPage;
    }

    @Override
    public void clearData() {
    }

    @Override
    public void reloadMainData() {
    }

    @Override
    public void close() {
        Stage stage = (Stage) NTV_TF_Name.getScene().getWindow();
        stage.close();
    }

    @FXML
    void OK() {
        switch (selectedType) {
            case 0:
                taskViewController.setTotal(true);
                break;
            case 1:
                taskViewController.setPartial(true);
                break;
            case 2:
                taskViewController.setIncremental(true);
                taskViewController.setLevel(selectedLevel);
                break;
            case 3:
                taskViewController.setLogic(true);
                break;
        }
        Print.apply("Triying to go Next");
        taskViewController.nextScreen();
    }

    @FXML
    void Cancel() {
        taskViewController.close();
        this.close();
    }
    /**Added Comment**/
}
