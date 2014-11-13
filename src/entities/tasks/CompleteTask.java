package entities.tasks;

import java.util.Date;

/**
 * Created by Casa on 01/11/2014.
 */
public class CompleteTask extends Task {
    public CompleteTask(Integer scheduleType, Date startDate) {
        super(Task.TOTAL, scheduleType, startDate, 0);
    }

    @Override
    public String toXML() {
        return "Full";
    }
}
