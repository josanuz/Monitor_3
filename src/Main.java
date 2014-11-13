import connection.derby.DerbyConnection;
import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Main extends Application {
    private static DerbyConnection embed;
    private static Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
        Locale.setDefault(Locale.forLanguageTag("es-CR"));
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        primaryStage.setTitle("Monitor 3");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.show();
        primaryStage.setOnCloseRequest((e) -> {
            try {
                embed.close();
                MainViewController.executorService.shutdownNow();
                MainViewController.executorService.shutdown();
                MainViewController.executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
        // NewTaskDialogController nt = new NewTaskDialogController();
        // nt.Show();
    }

    public static void main(String[] args) {
        try {
            embed = DerbyConnection.instance();
            connection = embed.getConnection();
            firstTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        launch(args);
    }

    private static void firstTime() throws SQLException {
        Connection c = null;
        try {
            c = embed.getConnection();
            c.createStatement().execute("SELECT * FROM TASKS");
        } catch (SQLException e) {
            c = embed.getConnection();
            System.out.println("TABLES NOT FOUND CREATING REQUIERED TABLES");
            c.createStatement().executeUpdate("CREATE TABLE TASKS(TaskID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) ,ScheduledType INT,Schedule VARCHAR(20),TypeID Int,TYPE VARCHAR(20),startDate TIMESTAMP, nextDate TIMESTAMP, TLevel INTEGER ,CONSTRAINT TASK_PK PRIMARY KEY (TaskID))");
            c.createStatement().executeUpdate("CREATE TABLE PARTIALTASKINCLUDES(TaskID INT, TablesSpace INT,INIT SMALLINT, DATAFILES SMALLINT, CONTROLFILE SMALLINT, ARCHIVELOGS SMALLINT,CONSTRAINT PTI_FK FOREIGN KEY (TaskID) REFERENCES TASKS)");
            c.createStatement().executeUpdate("CREATE TABLE TABLESPACE(TaskID INTEGER , TableSpaceName VARCHAR(128),CONSTRAINT TABLESPACE_PK PRIMARY KEY(TaskID,TableSpaceName),CONSTRAINT TABLESPACE_FK FOREIGN KEY (TaskID) REFERENCES TASKS)");
            c.createStatement().executeUpdate("CREATE TABLE DATAFILE(TaskID INTEGER , DataFileName VARCHAR(128),CONSTRAINT DATAFILE_PK PRIMARY KEY(DataFileName),CONSTRAINT DATAFILE_FK FOREIGN KEY (TaskID) REFERENCES TASKS)");
            c.createStatement().executeUpdate("CREATE TABLE SERVER(SERVERNAME VARCHAR(32),IP VARCHAR(64),PORT INTEGER, USERNAME VARCHAR(20),PSS VARCHAR(20),CONSTRAINT SERVER_PK PRIMARY KEY (SERVERNAME))");
            c.createStatement().executeUpdate("CREATE TABLE SERVER_TASKS(SERVER_NAME VARCHAR(32),TASKID INTEGER ,CONSTRAINT SERVER_FK FOREIGN KEY(SERVER_NAME)REFERENCES SERVER, CONSTRAINT TASKS_FK FOREIGN KEY(TASKID) REFERENCES TASKS)");
        }
        c.close();
    }
}
