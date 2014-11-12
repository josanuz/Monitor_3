package entities;

import entities.tasks.Task;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {
    private StringProperty serverName = new SimpleStringProperty();
    private StringProperty ip = new SimpleStringProperty();
    private IntegerProperty port = new SimpleIntegerProperty();
    private StringProperty userName = new SimpleStringProperty();
    private StringProperty pass = new SimpleStringProperty();
    private StringProperty schema = new SimpleStringProperty();
    private StringProperty service = new SimpleStringProperty();
    private ObservableList<Task> serverTasks;

    public String getPass() {
        return pass.get();
    }

    public StringProperty passProperty() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass.set(pass);
    }
    //public void setServerTasks(List<Task> serverTasks) {
    //this.serverTasks = serverTasks;
//    }

    public Server(String serverName, String ip, Integer port, String userName) {
        this.serverName.setValue(serverName);
        this.ip.setValue(ip);
        this.port.setValue(port);
        this.userName.setValue(userName);
        this.schema.setValue("DEFAULT");
        this.service.setValue("XE");
        this.serverTasks = FXCollections.<Task>observableArrayList();
    }

    public String getServerName() {
        return serverName.get();
    }

    public StringProperty serverNameProperty() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName.set(serverName);
    }

    public String getIp() {
        return ip.get();
    }

    public StringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public int getPort() {
        return port.get();
    }

    public IntegerProperty portProperty() {
        return port;
    }

    public void setPort(int port) {
        this.port.set(port);
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getSchema() {
        return schema.get();
    }

    public StringProperty schemaProperty() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema.set(schema);
    }

    public String getService() {
        return service.get();
    }

    public StringProperty serviceProperty() {
        return service;
    }

    public void setService(String service) {
        this.service.set(service);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        if (!ip.equals(server.ip)) return false;
        if (!port.equals(server.port)) return false;
        if (!serverName.equals(server.serverName)) return false;
        if (!userName.equals(server.userName)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = serverName.hashCode();
        result = 31 * result + ip.hashCode();
        result = 31 * result + port.hashCode();
        result = 31 * result + userName.hashCode();
        return result;
    }

    public ObservableList<Task> getServerTasks() {
        return serverTasks;
    }
}
