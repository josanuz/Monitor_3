package entities.tasks;

import java.util.Date;

/**
 * Created by Casa on 01/11/2014.
 */
public class IncrementalTask extends Task {
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    int level;

    public IncrementalTask(Integer scheduleType, Date startDate, int level) {
        super(Task.INCREMENTAL, scheduleType, startDate, level);
    }

    @Override
    public String toXML() {
        return "Incremental " + this.level;
    }
}
