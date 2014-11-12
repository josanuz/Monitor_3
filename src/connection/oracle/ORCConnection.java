package connection.oracle;

import entities.Server;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Casa on 13/09/2014.
 */
public class ORCConnection {
    //private static Connection connection = null;
    private String user = "/";
    private String roll = "sysdba";
    private String pass = "root";
    private String url = "localhost";
    private String service_name = "XE";
    private int port = 1521;
    //private Statement statement;
    private BasicDataSource ds = null;
    boolean initialized = false;
    private static HashMap<String, ORCConnection> serversConnection = new HashMap<>();

    /**
     * Single Instance Method get
     *
     * @return The ORCConnection Instance
     */
    public static ORCConnection Instance(Server s) {
        if (serversConnection.get(s.getServerName()) == null)
            serversConnection.put(s.getServerName(), new ORCConnection());
        return serversConnection.get(s.getServerName());
    }

    /**
     * Initiates a Connection to the Database
     *
     * @param user         The User in the DataBase
     * @param roll         The User Roll in the database
     * @param pass         The user Password
     * @param url          The Url to the server
     * @param service_name The Dabatase Service Name <em>Example XE on 11g Express Edition</em>
     * @param port         The listening port on the Server specified by the URL
     * @param sysdba       If user is Sysdba or Sysoper
     * @return <em>True</em> if connection success <em>False</em> otherwise
     */
    public boolean initializeConnection(String user, String roll, String pass, String url, String service_name, int port, boolean sysdba) throws ClassNotFoundException, SQLException {
        //if(initialized) close();
        this.user = user;
        this.roll = roll;
        this.pass = pass;
        this.url = url;
        this.service_name = service_name;
        this.port = port;
        String durl = "jdbc:oracle:thin:@" + this.url + ":" + this.port + "/" + this.service_name;
        ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setPassword(pass);
        ds.setUsername(user);
        ds.setUrl(durl);
        ds.setInitialSize(5);
        ds.setMaxTotal(20);
        ds.setAbandonedLogWriter(new PrintWriter(System.out));
        try {
            ds.setLogWriter(new PrintWriter(System.out));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection con = ds.getConnection();
        initialized = true;
        con.close();
        return initialized;
    }

    /**
     * Returns a new Connection to the database
     *
     * @return A Connection to the database
     * @throws SQLException
     */
    public synchronized Connection getOrcConnection() throws SQLException {
        return ds.getConnection();
    }
    //public synchronized void releaseOrcConnection(Object cnx){
    //}

    /**
     * Default Constructor
     */
    protected ORCConnection() {
    }

    /**
     * Parametrized Constructor
     *
     * @param s The Server to Establish a connection with
     */
    protected ORCConnection(Server s) {
        this.user = s.getUserName();
        this.roll = "sysdba";
        this.pass = s.getPass();
        this.url = s.getIp();
        this.service_name = s.getService();
        this.port = s.getPort();
        String durl = "jdbc:oracle:thin:@" + this.url + ":" + this.port + "/" + this.service_name;
        ds = new BasicDataSource();
        ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        ds.setPassword(pass);
        ds.setUsername(user);
        ds.setUrl(durl);
        ds.setInitialSize(5);
        ds.setMaxTotal(20);
        ds.setAbandonedLogWriter(new PrintWriter(System.out));
        try {
            ds.setLogWriter(new PrintWriter(System.out));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection con;
        initialized = false;
        try {
            con = ds.getConnection();
            initialized = true;
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Indicates If the Connection was successful established
     *
     * @return <b>True</b> id the connection is open <b>False</b> otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Closes The Connection With the Database
     */
    public void close() {
        try {
            ds.close();
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }
}
