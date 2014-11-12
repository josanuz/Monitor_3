package connection.derby;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Casa on 03/11/2014.
 */
public class DerbyConnection {
    private static DerbyConnection instance = null;
    private BasicDataSource ds = null;

    private DerbyConnection() throws SQLException {
        ds = new BasicDataSource();
        ds.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
        ds.setUrl("jdbc:derby:Monitor3DB;create=true");
        ds.setInitialSize(5);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
        //return connect;
    }

    public void returnConnection(Connection c) throws SQLException {
        c.close();
    }

    public static DerbyConnection instance() throws SQLException {
        if (instance == null) instance = new DerbyConnection();
        return instance;
    }

    public void close() throws SQLException {
        ds.close();
    }
}
