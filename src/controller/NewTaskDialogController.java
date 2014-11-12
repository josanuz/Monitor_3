package controller; /**
 * Created by Jose on 13/09/2014.
 */

import entities.Server;
import entities.TableSpace;
import entities.tasks.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class NewTaskDialogController implements Initializable, ControlledScreen {
    //Holds the screens to be displayed
    AnchorPane content;
    NewTaskDialogController parentController;
    String lastLoaded = "1";
    private boolean isTotal;
    private boolean isPartial;
    private boolean isIncremental;
    private boolean isLogic;
    private Server server;
    private Parent parentContainer;
    //CF,Init,Archive
    private boolean options[] = new boolean[3];
    int Level;
    int scheduleType;
    Date startDate;
    Task newTask;
    private List<TableSpace> tableSpaceList;
    Stage stage;
    private HashMap<String, Node> screens = new HashMap<>();
    private HashMap<String, String> RegisteredScreens = new HashMap<>();
    private HashMap<String, ControlledScreen> controllers = new HashMap<>();

    public NewTaskDialogController(Parent p, Server S) {
        content = new AnchorPane();
        RegisteredScreens.put("ConfirmNewTask", "/view/ConfirmNewTask/ConfirmNewTask.fxml");
        RegisteredScreens.put("NewTask", "/view/NewTaskView/AlertDialog_css.fxml");
        RegisteredScreens.put("PartialSelection", "/view/PartialSeletionView/PartialSeletionView.fxml");
        RegisteredScreens.put("Schedule", "/view/ScheduleView/Schedule.fxml");
        RegisteredScreens.put("4", "/view/ConfirmNewTask/ConfirmNewTask.fxml");
        RegisteredScreens.put("1", "/view/NewTaskView/AlertDialog_css.fxml");
        RegisteredScreens.put("3", "/view/PartialSeletionView/PartialSeletionView.fxml");
        RegisteredScreens.put("2", "/view/ScheduleView/Schedule.fxml");
        stage = new Stage();
        stage.setTitle("Crear Nueva Tarea");
        loadScreen("1", RegisteredScreens.get("1"));
        loadScreen("2", RegisteredScreens.get("2"));
        loadScreen("3", RegisteredScreens.get("3"));
        loadScreen("4", RegisteredScreens.get("4"));
        isTotal = isIncremental = isPartial = isLogic = false;
        Level = 0;
        tableSpaceList = new ArrayList<>();
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public List<TableSpace> getTableSpaceList() {
        return tableSpaceList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Task getNewTask() {
        return newTask;
    }

    public boolean isLogic() {
        return isLogic;
    }

    public void setLogic(boolean isLogic) {
        this.isLogic = isLogic;
    }

    public boolean isIncremental() {
        return isIncremental;
    }

    public void setIncremental(boolean isIncremental) {
        this.isIncremental = isIncremental;
    }

    public boolean isTotal() {
        return isTotal;
    }

    public void setTotal(boolean isTotal) {
        this.isTotal = isTotal;
    }

    public boolean isPartial() {
        return isPartial;
    }

    public void setPartial(boolean isPartial) {
        this.isPartial = isPartial;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getLastLoaded() {
        return lastLoaded;
    }

    public void setLastLoaded(String lastLoaded) {
        this.lastLoaded = lastLoaded;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public HashMap<String, String> getRegisteredScreens() {
        return RegisteredScreens;
    }

    @Override
    public void setScreenParent(NewTaskDialogController screenPage) {
        parentController = screenPage;
    }

    @Override
    public void clearData() {
    }

    @Override
    public void reloadMainData() {
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void Show() {
        //Block Owner http://stackoverflow.com/questions/15625987/block-owner-window-java-fx
        this.setScreen("1");
        stage.setScene(new Scene(this.content, 499, 195));
        stage.show();
    }

    //Add the screen to the collection
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name);
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(String name, String resource) {
        if (screens.containsKey(name)) return true;
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenControler = ((ControlledScreen) myLoader.getController());
            myScreenControler.setScreenParent(this);
            addScreen(name, loadScreen);
            addControler(name, myScreenControler);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.err.print("Error");
            e.printStackTrace();
            return false;
        }
    }

    private void addControler(String name, ControlledScreen myScreenControler) {
        this.controllers.put(name, myScreenControler);
    }

    private boolean removeControler(String name) {
        return this.controllers.remove(name) != null;
    }

    //This method tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    // If there isn't any screen being displayed, the new screen is just added to the root.
    public boolean setScreen(final String name) {
        //content.getScene().getWindow().setOnCloseRequest((e) -> {
        //    this.close();
        //});
        if (screens.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = content.opacityProperty();
            if (!content.getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(1), t -> {//Duration Changed 1000->1
                            content.getChildren().remove(0);                    //remove the displayed screen
                            content.getChildren().add(0, screens.get(name));     //add the screen
                            content.setTopAnchor(content.getChildren().get(0), 0.0);
                            content.setLeftAnchor(content.getChildren().get(0), 0.0);
                            content.setBottomAnchor(content.getChildren().get(0), 0.0);
                            content.setRightAnchor(content.getChildren().get(0), 0.0);
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(800), new KeyValue(opacity, 1.0)));
                            fadeIn.play();
                        }, new KeyValue(opacity, 0.0)));
                fade.play();
            } else {
                content.setOpacity(0.0);
                content.getChildren().add(screens.get(name));       //no one else been displayed, then just show
                content.setTopAnchor(content.getChildren().get(0), 0.0);
                content.setLeftAnchor(content.getChildren().get(0), 0.0);
                content.setBottomAnchor(content.getChildren().get(0), 0.0);
                content.setRightAnchor(content.getChildren().get(0), 0.0);
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(1), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }
    }

    //This method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        return screens.remove(name) != null;
    }

    public Scene getScene() {
        return this.content.getScene();
    }

    void clearLastScreenData() {
        if (lastLoaded.equals("")) return;
        ControlledScreen ctr = controllers.get(lastLoaded);
        ctr.clearData();
    }

    void reloadMainData(String name) {
        ControlledScreen ctr = controllers.get(name);
        ctr.reloadMainData();
    }

    public void nextScreen() {
        if (lastLoaded.equals("1")) {
            setSize("2");
            setScreen("2");
            lastLoaded = "2";
            return;
        }
        if (lastLoaded.equals("2")) {
            if (isPartial) {
                setSize("3");
                setScreen("3");
                lastLoaded = "3";
                return;
            } else {
                lastLoaded = "4";
                setScreen("4");
                return;
            }
        }
        if (lastLoaded.equals("3")) {
            lastLoaded = "4";
            setScreen("4");
            return;
        }
        //Si es la 4 no hay nextScreen
    }

    public void previousScreen() {
        if (lastLoaded.equals("2")) {
            setSize("1");
            setScreen("1");
            lastLoaded = "1";
        }
        if (lastLoaded.equals("4")) {
            if (isPartial) {
                setSize("3");
                setScreen("3");
                lastLoaded = "3";
            } else {
                setSize("2");
                lastLoaded = "2";
                setScreen("2");
            }
        }
        if (lastLoaded.equals("3")) {
            setSize("2");
            lastLoaded = "2";
            setScreen("2");
        }
        //Si es la 1 no hay PreviiousScreen
    }

    void setSize(String s) {
        if (s.equals("1")) {
            stage.setHeight(250);
            stage.setWidth(510);
        } else if (s.equals("2")) {
            stage.setHeight(312);
            stage.setWidth(422);
        } else if (s.equals("3")) {
            stage.setHeight(400);
            stage.setWidth(515);
        }
    }

    public boolean[] getOptions() {
        return options;
    }

    public void setOptions(boolean[] options) {
        this.options = options;
    }

    public NewTaskDialogController getParentController() {
        return parentController;
    }

    public void setParentController(NewTaskDialogController parentController) {
        this.parentController = parentController;
    }

    public Parent getParentContainer() {
        return parentContainer;
    }

    public void setParentContainer(Parent parentContainer) {
        this.parentContainer = parentContainer;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void createTask() {
        if (isTotal)
            this.newTask = new CompleteTask(this.scheduleType, this.startDate);
        if (isPartial) {
            newTask = new PartialTask(this.scheduleType, this.startDate);
            ((PartialTask) newTask).getAffectedTablespaces().addAll(this.getTableSpaceList());
            ((PartialTask) newTask).setIncludeControlFiles(options[0]);
            ((PartialTask) newTask).setIncludeInitFile(options[1]);
            ((PartialTask) newTask).setIncludeArchiveLogs(options[2]);
        }
        if (isIncremental) {
            newTask = new IncrementalTask(this.scheduleType, this.startDate, this.Level);
        }
        if (isLogic) {
            newTask = new LogicalTask(this.scheduleType, this.startDate);
        }
        server.getServerTasks().add(newTask);
    }
    /**Addedd coommet**/
}