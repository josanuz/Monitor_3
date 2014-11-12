package accessobjects;

import connection.derby.DerbyConnection;
import entities.TableSpace;
import entities.tasks.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Casa on 03/11/2014.
 */
public class TaskDAO implements DAO<Task, Integer> {
    private Connection c;
    static TaskDAO instance;
    private PreparedStatement getBy;
    private PreparedStatement getAll;
    private PreparedStatement update;
    private PreparedStatement delete;
    private PreparedStatement tableSpaces;
    private PreparedStatement dataFiles;
    private PreparedStatement includes;
    private PreparedStatement insert;

    public static TaskDAO instance() throws SQLException {
        if (instance == null) instance = new TaskDAO();
        return instance;
    }

    public TaskDAO() throws SQLException {
        c = DerbyConnection.instance().getConnection();
        getBy = c.prepareStatement("SELECT * FROM TASKS WHERE TASKID = ?");
        getAll = c.prepareStatement("SELECT * FROM TASKS");
        update = c.prepareStatement("UPDATE TASKS SET ScheduledType = ?,Schedule = ?,TypeID = ?,TYPE = ?,startDate = ?, nextDate = ?, TLevel = ? WHERE TASKID = ?");
        delete = c.prepareStatement("DELETE FROM TASKS WHERE TASKID = ?");
        tableSpaces = c.prepareStatement("SELECT * FROM TABLESPACE WHERE TASKID = ?");
        dataFiles = c.prepareStatement("SELECT * FROM DATAFILE WHERE TASKID = ?");
        insert = c.prepareStatement("INSERT INTO TASKS(ScheduledType,Schedule ,TypeID ,TYPE ,startDate , nextDate, TLevel ) values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
    }

    @Override
    public Task getBy(Integer pk) throws SQLException {
        int tskid = -1, schty = -1, typeid = -1, level = 0;
        String sch = "", type = "";
        Timestamp start = null;
        Timestamp next = null;
        Task t = null;
        ResultSet r;
        getBy.setInt(1, pk);
        r = getBy.executeQuery();
        while (r.next()) {
            schty = r.getInt("ScheduledType");
            typeid = r.getInt("TypeID");
            tskid = pk;
            sch = r.getString("Schedule");
            type = r.getString("TYPE");
            start = r.getTimestamp("startDate");
            next = r.getTimestamp("nextDate");
            level = r.getInt("TLevel");
            if (typeid == Task.PARTIAL) {
                PartialTask p = new PartialTask(typeid, new Date(start.getTime()));
                final int pTskId = tskid;
                List<TableSpace> taskTableSpaces = TableSpaceDAO.instance().select((TableSpace tbs) -> tbs.getTaskID() == pTskId);
                p.setNextDate(new Date(next.getTime()));
                p.getAffectedTablespaces().addAll(taskTableSpaces);
                p.setTaskID(pk);
                t = p;
                //TODO add DataFiles to TBS
            } else if (typeid == Task.TOTAL) {
                t = new CompleteTask(schty, new Date(next.getTime()));
                t.setTaskID(pk);
            } else if (typeid == Task.LOGIC) {
                t = new LogicalTask(schty, new Date(next.getTime()));
                t.setTaskID(pk);
            } else {
                t = new IncrementalTask(schty, new Date(next.getTime()), level);
                t.setTaskID(pk);
            }
        }
        return t;
    }

    @Override
    public boolean insert(Task what) throws SQLException {
        insert.setInt(1, what.getScheduledTypeID());
        insert.setString(2, what.getScheduleType());
        insert.setInt(3, what.getTypeID());
        insert.setString(4, what.getType());
        insert.setTimestamp(5, new Timestamp(what.getStart_Date().getTime()));
        insert.setTimestamp(6, new Timestamp(what.getNextDate().getTime()));
        insert.setInt(7, what.getLevel());
        boolean result = insert.executeUpdate() >= 0;
        ResultSet rs = insert.getGeneratedKeys();
        while (rs.next()) what.setTaskID(rs.getInt(1));
        return result;
    }

    @Override
    public List<Task> getAll() throws SQLException {
        List<Integer> ids = new ArrayList<>();
        List<Task> tasks = new ArrayList<>();
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery("SELECT TASKID FROM TASKS");
        while (rs.next()) {
            ids.add(rs.getInt(1));
        }
        ids.stream().forEach(i -> {
            try {
                tasks.add(getBy(i));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return tasks;
    }

    @Override
    public boolean update(Task what) throws SQLException {
        update.setInt(1, what.getScheduledTypeID());
        update.setString(2, what.getScheduleType());
        update.setInt(3, what.getTypeID());
        update.setString(4, what.getType());
        update.setTimestamp(5, new Timestamp(what.getStart_Date().getTime()));
        update.setTimestamp(6, new Timestamp(what.getNextDate().getTime()));
        update.setInt(7, what.getLevel());
        update.setInt(8, what.getTaskID());
        return update.executeUpdate() > 0;
    }

    @Override
    public boolean delete(Task what) throws SQLException {
        delete.setInt(1, what.getTaskID());
        return delete.executeUpdate() > 0;
    }

    @Override
    public List<Task> select(Predicate<Task> predicate) throws SQLException {
        return getAll().stream().filter(predicate).collect(Collectors.toList());
    }

    @Override
    public void deleteAll() throws SQLException {
        c.createStatement().execute("DELETE FROM TASKS");
        c.createStatement().execute("DELETE FROM TABLESPACE");
        c.createStatement().execute("DELETE FROM DATAFILE");
    }
}
