package controller;

import accessobjects.ServerDAO;
import accessobjects.TaskDAO;
import entities.MessageWriter;
import entities.NSV;
import entities.Server;
import entities.TableSpace;
import entities.tasks.PartialTask;
import entities.tasks.Task;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Casa on 12/11/2014.
 */
public class MainViewController implements Initializable {
    private static Server actualSelectedServer;
    private
    @FXML
    ListView<Server> MV_LV_Servers;
    private
    @FXML
    TableView<Task> MV_TV_Tasks;
    private
    @FXML
    TableColumn<Task, Integer> MV_TC_TaskID;
    private
    @FXML
    TableColumn<Task, String> MV_TC_TaskType;
    private
    @FXML
    TableColumn<Task, String> MV_TC_Start;
    private
    @FXML
    TableColumn<Task, String> MV_TC_Next;
    private
    @FXML
    TableColumn<Task, String> MV_TC_TSchedule;
    private
    @FXML
    Label MV_LBL_STLevel;
    private
    @FXML
    Label MV_LBL_STSchedule;
    private
    @FXML
    Label MV_LBL_STID;
    private
    @FXML
    Label MV_LBL_TID;
    private
    @FXML
    Label MV_LBL_TLevel;
    private
    @FXML
    Label MV_LBL_TSchedule;
    private
    @FXML
    CheckBox MV_CHC_CF;
    private
    @FXML
    CheckBox MV_CHK_IF;
    private
    @FXML
    CheckBox MV_CHK_AL;
    private
    @FXML
    ListView<TableSpace> MV_LV_TIncludedTBS;
    public static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    /*static {
        actualSelectedServer = new Server("Oracle", "192.168.1.111", 1521, "SYS as sysdba");
        actualSelectedServer.setService("XE");
        actualSelectedServer.setPass("root");
    }*/
    public static Server actualServer() {
        return actualSelectedServer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            MV_LV_Servers.getItems().addAll(ServerDAO.instance().getAll());
            MV_LV_Servers.getSelectionModel().selectedItemProperty().addListener((selected, old, newValue) -> {
                actualSelectedServer = newValue;
                MV_TV_Tasks.getItems().setAll(newValue.getServerTasks());
            });
            MV_TC_Next.setCellValueFactory(new PropertyValueFactory<Task, String>("next"));
            MV_TC_TaskID.setCellValueFactory(new PropertyValueFactory<Task, Integer>("taskID"));
            MV_TC_TaskType.setCellValueFactory(new PropertyValueFactory<Task, String>("type"));
            MV_TC_Start.setCellValueFactory(new PropertyValueFactory<Task, String>("startDate"));
            MV_TC_TSchedule.setCellValueFactory(new PropertyValueFactory<Task, String>("scheduleType"));
            MV_TV_Tasks.getSelectionModel().selectedItemProperty().addListener((e, o, n) -> {
                infoPane(n);
            });
            MV_CHC_CF.setVisible(false);
            MV_CHK_AL.setVisible(false);
            MV_CHK_IF.setVisible(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (MV_CHC_CF.getScene() == null) System.out.println("YAY!");
    }

    private void infoPane(Task n) {
        MV_LBL_TID.setText("" + n.getTaskID());
        MV_LBL_TLevel.setVisible(false);
        MV_LBL_STLevel.setVisible(false);
        MV_LV_TIncludedTBS.setVisible(false);
        MV_CHC_CF.setVisible(false);
        MV_CHK_AL.setVisible(false);
        MV_CHK_IF.setVisible(false);
        MV_LBL_TSchedule.setText(n.getScheduleType());
        if (n.getTypeID() == Task.PARTIAL) {
            PartialTask ptemp = (PartialTask) n;
            MV_LBL_TLevel.setVisible(false);
            MV_LBL_STLevel.setVisible(false);
            MV_LV_TIncludedTBS.setVisible(true);
            MV_CHC_CF.setVisible(true);
            MV_CHK_AL.setVisible(true);
            MV_CHK_IF.setVisible(true);
            MV_CHC_CF.setSelected(ptemp.getIncludeControlFiles());
            MV_CHK_AL.setSelected(ptemp.getIncludeArchiveLogs());
            MV_CHK_IF.setSelected(ptemp.getIncludeInitFile());
            MV_LV_TIncludedTBS.getItems().setAll(ptemp.getAffectedTablespaces());
        }
        if (n.getTypeID() == Task.INCREMENTAL) {
            PartialTask ptemp = (PartialTask) n;
            MV_LBL_TLevel.setVisible(true);
            MV_LBL_TLevel.setText("" + n.getLevel());
            MV_LBL_STLevel.setVisible(true);
            MV_LV_TIncludedTBS.setVisible(false);
            MV_CHC_CF.setVisible(false);
            MV_CHK_AL.setVisible(false);
            MV_CHK_IF.setVisible(false);
        }
        executorService.scheduleAtFixedRate(checker, 1, 1, TimeUnit.HOURS);
    }

    Runnable checker = () -> {
        System.err.print("Running like a boss");
        try {
            for (Server s : ServerDAO.instance().getAll()) {
                MessageWriter ms = MessageWriter.instance(s);
                for (Task t : s.getServerTasks()) {
                    if (Math.abs(t.getNextDate().getTime() - System.currentTimeMillis()) < 60000) {
                        t.ExectuteTime();
                        ms.WriteMessage(t.toXML());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    @FXML
    private void new_Task() {
        if (MV_LV_Servers.getSelectionModel().getSelectedItem() == null) {
            Dialogs.create().title("Error").message("Debe Seleccionar un servidor").showError();
        }
        NewTaskDialogController newTaskDialogController = new NewTaskDialogController(MV_CHC_CF.getScene().getWindow(), actualSelectedServer);
        newTaskDialogController.Show();
        try {
            this.MV_TV_Tasks.getItems().setAll(ServerDAO.instance().getBy(this.actualSelectedServer.getServerName()).getServerTasks());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ChangeListener<Boolean> cmbChangeListener = (s, o, n) -> {
        ((CheckBox) s).setSelected(o);
    };

    @FXML
    void newServer() {
        NSV nsv = new NSV();
        Server s = nsv.show();
        if (s.getServerName().trim() == "") return;
        try {
            ServerDAO.instance().insert(s);
            this.MV_LV_Servers.getItems().setAll(ServerDAO.instance().getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DeleteTask() throws SQLException {
        Task t = this.MV_TV_Tasks.getSelectionModel().getSelectedItem();
        if (t == null) {
            Dialogs.create().message("Debe Seleccionar una Tarea para Borrar").showError();
            return;
        }
        if (Dialogs.create().showConfirm() == Dialog.Actions.YES) {
            TaskDAO.instance().delete(t);
            MV_TV_Tasks.getItems().setAll(ServerDAO.instance().getBy(actualSelectedServer.getServerName()).getServerTasks());
        }
    }

    @FXML
    void executeTask() {
        Task t = MV_TV_Tasks.getSelectionModel().getSelectedItem();
        if (t != null) {
            try {
                MessageWriter messageWriter = MessageWriter.instance(actualSelectedServer);
                messageWriter.WriteMessage(t.toXML());
                t.ExectuteTime();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

