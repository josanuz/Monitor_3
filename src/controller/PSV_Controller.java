package controller;

import connection.oracle.ORCConnection;
import entities.Server;
import entities.TableSpace;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Casa on 11/11/2014.
 */
public class PSV_Controller implements Initializable, ControlledScreen {
    private NewTaskDialogController tvController;
    @FXML
    ListView<TableSpace> PSV_LV_TableSpaces;
    @FXML
    CheckBox PSV_CHK_ControlFiles;
    @FXML
    CheckBox PSV_CHK_InitFile;
    @FXML
    CheckBox PSV_CHK_ArchivedLogs;
    private Server s;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Server s = MainViewController.actualServer();
        try {
            List<TableSpace> tableSpaceList = tableSpaces();
            PSV_LV_TableSpaces.getItems().addAll(tableSpaceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void OK() {
        tvController.getTableSpaceList().addAll(PSV_LV_TableSpaces.getSelectionModel().getSelectedItems());
        boolean options[] = new boolean[3];
        options[0] = PSV_CHK_ControlFiles.isSelected();
        options[1] = PSV_CHK_InitFile.isSelected();
        options[2] = PSV_CHK_ArchivedLogs.isSelected();
        tvController.setOptions(options);
    }

    @FXML
    void Cancel() {
        tvController.close();
    }

    @FXML
    void Back() {
        tvController.previousScreen();
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

    private List<TableSpace> tableSpaces() throws SQLException {
        List<TableSpace> tableSpaces = new ArrayList<>();
        Connection c = ORCConnection.Instance(s).getOrcConnection();
        ResultSet rs = c.createStatement().executeQuery("SELECT TABLESPACE_NAME FROM DBA_TABLESPACES");
        while (rs.next()) {
            String s = rs.getString(1);
            tableSpaces.add(new TableSpace(-1, s));
        }
        return tableSpaces;
    }
    /**Added Comment**/
}
