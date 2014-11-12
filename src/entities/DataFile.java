package entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Casa on 03/11/2014.
 */
public class DataFile {
    StringProperty name;

    public DataFile(String name) {
        this.name = new SimpleStringProperty();
        this.name.setValue(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
