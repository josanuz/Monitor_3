package entities.tasks;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Johan on 01/11/2014.
 */
public abstract class Task {
    public static ObservableList<String> TypeList = FXCollections.observableArrayList(Arrays.asList("Completo", "Incremental", "Parcial", "Lógico"));
    public static ObservableList<String> scheduledTypeList = FXCollections.observableArrayList(Arrays.asList("Diario", "Semanal", "Mensual"));
    public static int INCREMENTAL = 1;
    public static int PARTIAL = 2;
    public static int TOTAL = 0;
    public static int LOGIC = 3;
    public static int DIARY = 0;
    public static int WEEKLY = 1;
    public static int MONTHLY = 2;
    public static SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy-H:mm:ss");
    private IntegerProperty taskID = new SimpleIntegerProperty();
    private StringProperty type = new SimpleStringProperty();
    private StringProperty startDate = new SimpleStringProperty();
    private StringProperty scheduleType = new SimpleStringProperty();
    private IntegerProperty scheduledTypeID = new SimpleIntegerProperty();
    private IntegerProperty typeID = new SimpleIntegerProperty();
    private StringProperty next = new SimpleStringProperty();
    private Date start_Date;
    private Date nextDate;
    private int level;

    public int getTaskID() {
        return taskID.get();
    }

    public IntegerProperty taskIDProperty() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID.set(taskID);
    }

    public Task() {
    }

    public Task(Integer type, Integer scheduleType, Date startDate, int Level) {
        try {
            this.start_Date = startDate;
            this.type.setValue(TypeList.get(type));
            this.typeID.setValue(type);
            this.startDate.set(dateTimeFormatter.format(startDate));
            this.scheduledTypeID.setValue(scheduleType);
            this.scheduleType.setValue(Task.scheduledTypeList.get(scheduleType));
            this.nextDate = next();
            this.next.setValue(dateTimeFormatter.format(nextDate));
            this.startDate.setValue(dateTimeFormatter.format(start_Date));
            level = Level;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private Date next() throws ParseException {
        Calendar c1 = Calendar.getInstance();
        Date last;
        if (this.nextDate != null) last = nextDate;
        else {
            last = start_Date;
            return last;
        }
        c1.setTime(last);
        if (getScheduledTypeID() == Task.DIARY) c1.add(Calendar.DATE, 1);
        else if (getScheduledTypeID() == Task.WEEKLY) c1.add(Calendar.DATE, 7);
        else c1.add(Calendar.MONTH, 1);
        return new Date(c1.getTimeInMillis());
    }

    public void ExectuteTime() {
        try {
            this.nextDate = next();
            this.setNext(dateTimeFormatter.format(nextDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTypeID() {
        return typeID.get();
    }

    public IntegerProperty typeIDProperty() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID.set(typeID);
    }

    public Date getStart_Date() {
        return start_Date;
    }

    public void setStart_Date(Date start_Date) {
        this.setNext(dateTimeFormatter.format(start_Date));
        this.start_Date = start_Date;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.setNext(dateTimeFormatter.format(nextDate));
        this.nextDate = nextDate;
    }

    public static ObservableList<String> getTypeList() {
        return TypeList;
    }

    public static ObservableList<String> getScheduledTypeList() {
        return scheduledTypeList;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getScheduleType() {
        return scheduleType.get();
    }

    public StringProperty scheduleTypeProperty() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType.set(scheduleType);
    }

    public int getScheduledTypeID() {
        return scheduledTypeID.get();
    }

    public IntegerProperty scheduledTypeIDProperty() {
        return scheduledTypeID;
    }

    public void setScheduledTypeID(int scheduledTypeID) {
        this.scheduledTypeID.set(scheduledTypeID);
    }

    public String getNext() {
        return next.get();
    }

    public StringProperty nextProperty() {
        return next;
    }

    public void setNext(String next) {
        this.next.set(next);
    }

    abstract public String toXML();
}
