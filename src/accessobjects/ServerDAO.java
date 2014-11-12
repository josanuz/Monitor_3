package accessobjects;

import connection.derby.DerbyConnection;
import entities.Server;
import entities.TableSpace;
import entities.tasks.Task;

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
public class ServerDAO implements DAO<Server, String> {
    private static ServerDAO instance;

    public static ServerDAO instance() throws SQLException {
        if (instance == null) instance = new ServerDAO();
        return instance;
    }

    private Connection connection;
    private PreparedStatement getBy;
    private PreparedStatement getAll;
    private PreparedStatement insert;
    private PreparedStatement update;
    private PreparedStatement delete;
    private PreparedStatement deleteAll;

    private ServerDAO() throws SQLException {
        connection = DerbyConnection.instance().getConnection();
        getBy = connection.prepareStatement("SELECT * FROM SERVER WHERE SERVERNAME = ?");
        getAll = connection.prepareStatement("SELECT SERVERNAME FROM SERVER");
        insert = connection.prepareStatement("INSERT INTO SERVER VALUES(?,?,?,?,?)");
        update = connection.prepareStatement("UPDATE SERVER SET IP = ?,PORT = ?, USERNAME = ? ,PSS = ? WHERE  SERVERNAME = ?");
        delete = connection.prepareStatement("DELETE FROM SERVER WHERE  SERVERNAME = ?");
        deleteAll = connection.prepareStatement("DELETE FROM SERVER");
    }

    @Override
    public Server getBy(String pk) throws SQLException {
        Server s = null;
        getBy.setString(1, pk);
        String serverName, username, pss, IP;
        Integer Port;
        ResultSet rs = getBy.executeQuery();
        List<String> serverNames = new ArrayList<>();
        while (rs.next()) {
            serverName = rs.getString("SERVERNAME");
            username = rs.getString("USERNAME");
            pss = rs.getString("PPS");
            IP = rs.getString("IP");
            Port = rs.getInt("PORT");
            s = new Server(serverName, IP, Port, username);
            s.setPass(pss);
            serverNames.add(serverName);
        }
        for (String sname : serverNames) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT TASKID FROM SERVER_TASKS WHERE SERVER_NAME = \'" + sname + "\'");
            while (resultSet.next()) {
                Task t = TaskDAO.instance().getBy(resultSet.getInt(1));
                s.getServerTasks().add(t);
            }
        }
        return s;
    }

    @Override
    public boolean insert(Server what) throws SQLException {
        insert.setString(1, what.getServerName());
        insert.setString(2, what.getIp());
        insert.setInt(3, what.getPort());
        insert.setString(4, what.getUserName());
        insert.setString(5, what.getPass());
        return insert.executeUpdate() >= 0;
    }

    @Override
    public List<Server> getAll() throws SQLException {
        List<Server> servers = new ArrayList<>();
        ResultSet rs = getAll.executeQuery();
        while (rs.next()) {
            servers.add(getBy(rs.getString(rs.getString(1))));
        }
        return servers;
    }

    @Override
    public boolean update(Server what) throws SQLException {
        //TODO HACER UPDATE SERVER
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Server what) throws SQLException {
        delete.setString(1, what.getServerName());
        return delete.executeUpdate() >= 0;
    }

    @Override
    public List<Server> select(Predicate<Server> p) throws SQLException {
        return getAll().stream().filter(p).collect(Collectors.toList());
    }

    @Override
    public void deleteAll() throws SQLException {
        deleteAll.execute();
    }

    public boolean assingTableSpace(Server s, TableSpace t) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("INSERT INTO SERVER_TASKS VALUES (?,?)");
        ps.setString(1, s.getServerName());
        ps.setInt(2, t.getTaskID());
        return ps.executeUpdate() >= 0;
    }

    public boolean removeTableSpace(Server s, TableSpace t) throws SQLException {
        PreparedStatement ps = connection.prepareStatement("DELETE FROM SERVER_TASKS WHERE SERVER_NAME = ? AND TASKID = ?");
        ps.setString(1, s.getServerName());
        ps.setInt(2, t.getTaskID());
        return ps.executeUpdate() >= 0;
    }
}
