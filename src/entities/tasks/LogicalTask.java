package entities.tasks;

import java.util.Date;

/**
 * Created by Casa on 01/11/2014.
 */
public class LogicalTask extends Task {
    public LogicalTask(Integer scheduleType, Date startDate) {
        super(Task.LOGIC, scheduleType, startDate, 0);
    }

    @Override
    public String toXML() {
        return "Logical";
    }
}
