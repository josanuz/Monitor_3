package entities.tasks;

import entities.DataFile;
import entities.TableSpace;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Casa on 01/11/2014.
 */
public class PartialTask extends Task {
    private List<TableSpace> affectedTablespaces;
    private List<DataFile> affectedDataFiles;
    private BooleanProperty includeArchiveLogs = new SimpleBooleanProperty();
    private BooleanProperty includeInitFile = new SimpleBooleanProperty();
    private BooleanProperty includeControlFiles = new SimpleBooleanProperty();

    public PartialTask(Integer scheduleType, Date startDate) {
        super(Task.PARTIAL, scheduleType, startDate, 0);
        affectedTablespaces = new ArrayList<>();
        includeArchiveLogs.setValue(false);
        includeControlFiles.setValue(false);
        includeInitFile.setValue(false);
    }

    public PartialTask() {
        super(Task.TOTAL, Task.DIARY, new Date(), 0);
        this.affectedTablespaces = new ArrayList<>();
    }

    public List<TableSpace> getAffectedTablespaces() {
        return affectedTablespaces;
    }

    public boolean getIncludeArchiveLogs() {
        return includeArchiveLogs.get();
    }

    public BooleanProperty includeArchiveLogsProperty() {
        return includeArchiveLogs;
    }

    public void setIncludeArchiveLogs(boolean includeArchiveLogs) {
        this.includeArchiveLogs.set(includeArchiveLogs);
    }

    public boolean getIncludeInitFile() {
        return includeInitFile.get();
    }

    public BooleanProperty includeInitFileProperty() {
        return includeInitFile;
    }

    public void setIncludeInitFile(boolean includeInitFile) {
        this.includeInitFile.set(includeInitFile);
    }

    public boolean getIncludeControlFiles() {
        return includeControlFiles.get();
    }

    public BooleanProperty includeControlFilesProperty() {
        return includeControlFiles;
    }

    public void setIncludeControlFiles(boolean includeControlFiles) {
        this.includeControlFiles.set(includeControlFiles);
    }

    @Override
    public String toXML() {
        String s = "Partial " + this.getIncludeControlFiles() + " " + this.getIncludeInitFile() + " " + this.includeArchiveLogs;
        for (TableSpace t : this.affectedTablespaces)
            s += " " + t.getNombre();
        return s;
    }

    public List<DataFile> getAffectedDataFiles() {
        return affectedDataFiles;
    }
}
