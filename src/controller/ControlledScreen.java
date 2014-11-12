package controller;

/**
 * Created by Jose on 13/09/2014.
 */
public interface ControlledScreen {
    //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(NewTaskDialogController screenPage);

    public void clearData();

    public void reloadMainData();

    public void close();
    /*added comment*/
}