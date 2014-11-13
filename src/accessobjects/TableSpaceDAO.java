package accessobjects;

import connection.derby.DerbyConnection;
import entities.TableSpace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Casa on 03/11/2014.
 */
public class TableSpaceDAO implements DAO<TableSpace, String> {
    private Connection c;
    private static TableSpaceDAO instance = null;
    private PreparedStatement getby;
    private PreparedStatement getAll;
    private PreparedStatement insert;
    private PreparedStatement delete;

    public static TableSpaceDAO instance() throws SQLException {
        if (instance == null) instance = new TableSpaceDAO();
        return instance;
    }

    private TableSpaceDAO() throws SQLException {
        c = DerbyConnection.instance().getConnection();
        getAll = c.prepareStatement("SELECT * FROM TABLESPACE");
        delete = c.prepareStatement("DELETE FROM TABLESPACE WHERE TASKID = ? AND TABLESPACENAME = ?");
        insert = c.prepareStatement("INSERT INTO TABLESPACE(TASKID,TABLESPACENAME) VALUES(?,?)");
    }

    @Override
    public TableSpace getBy(String pk) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean insert(TableSpace what) throws SQLException {
        insert.setInt(1, what.getTaskID());
        insert.setString(2, what.getNombre());
        return insert.executeUpdate() > 0;
    }

    @Override
    public List<TableSpace> getAll() throws SQLException {
        List<TableSpace> tableSpaces = new ArrayList<>();
        ResultSet rs = getAll.executeQuery();
        while (rs.next()) {
            Integer taskid = rs.getInt("TASKID");
            String name = rs.getString("TableSpaceName");
            TableSpace t = new TableSpace(taskid, name);
            tableSpaces.add(t);
        }
        return tableSpaces;
    }

    @Override
    public boolean update(TableSpace what) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(TableSpace what) throws SQLException {
        delete.setInt(1, what.getTaskID());
        delete.setString(2, what.getNombre());
        delete.executeUpdate();
        return delete.getUpdateCount() >= 0;
    }

    @Override
    public List<TableSpace> select(Predicate<TableSpace> p) throws SQLException {
        return getAll().stream().filter(p).collect(Collectors.toList());
    }

    @Override
    public void deleteAll() throws SQLException {
        c.createStatement().execute("DELETE FROM TABLESPACE");
    }
}
