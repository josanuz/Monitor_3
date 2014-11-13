package controller;

import entities.tasks.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by Casa on 11/11/2014.
 */
public class SV_Controller implements Initializable, ControlledScreen {
    NewTaskDialogController tvController;
    @FXML
    DatePicker SV_DP_StartDate;
    @FXML
    ChoiceBox<String> SV_CB_ScheduleType;
    @FXML
    ChoiceBox<Integer> SV_CB_SHour;
    @FXML
    ChoiceBox<Integer> SV_CB_SMin;
    private Date startDate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SV_CB_ScheduleType.getItems().addAll(Task.scheduledTypeList);
        SV_CB_ScheduleType.getSelectionModel().select(0);
        SV_DP_StartDate.setValue(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item.isBefore(SV_DP_StartDate.getValue())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        SV_DP_StartDate.setDayCellFactory(dayCellFactory);
        for (int i = 0; i < 24; i++) SV_CB_SHour.getItems().add(i);
        SV_CB_SHour.getSelectionModel().select(0);
        for (int i = 0; i < 60; i += 5) SV_CB_SMin.getItems().add(i);
        SV_CB_SMin.getSelectionModel().select(0);
    }

    @FXML
    void OK() {
        tvController.setScheduleType(SV_CB_ScheduleType.getSelectionModel().getSelectedIndex());
        LocalDate selectedDate = SV_DP_StartDate.getValue();
        LocalDateTime fStartDate = selectedDate.atTime(SV_CB_SHour.getValue(), SV_CB_SMin.getValue());
        Date daDate = Date.from(fStartDate.atZone(ZoneId.systemDefault()).toInstant());
        tvController.setStartDate(daDate);
        tvController.nextScreen();
    }

    @FXML
    void Back() {
        tvController.previousScreen();
    }

    @FXML
    void Cancel() {
        tvController.close();
    }

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
    /**added comment**/
}
