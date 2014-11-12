package entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Casa on 01/11/2014.
 * New COmment
 */
public class TableSpace {
    private List<String> dataFiles;
    private StringProperty Nombre = new SimpleStringProperty();
    private IntegerProperty TaskID = new SimpleIntegerProperty();

    public TableSpace(Integer tsk, String nombre) {
        Nombre.setValue(nombre);
        TaskID.setValue(tsk);
        dataFiles = new ArrayList<>();
    }

    public List<String> getDataFiles() {
        return dataFiles;
    }

    public String getNombre() {
        return Nombre.get();
    }

    public StringProperty nombreProperty() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre.set(nombre);
    }

    public int getTaskID() {
        return TaskID.get();
    }

    public IntegerProperty taskIDProperty() {
        return TaskID;
    }

    public void setTaskID(int taskID) {
        this.TaskID.set(taskID);
    }

    @Override
    public String toString() {
        return this.getNombre();
    }
}
