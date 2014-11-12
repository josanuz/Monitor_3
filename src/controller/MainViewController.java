package controller;

import entities.Server;

/**
 * Created by Casa on 12/11/2014.
 */
public class MainViewController {
    private static Server actualSelectedServer;

    static {
        actualSelectedServer = new Server("Oracle", "192.168.1.111", 1521, "SYS as sysdba");
        actualSelectedServer.setService("XE");
        actualSelectedServer.setPass("root");
    }

    public static Server actualServer() {
        return actualSelectedServer;
    }
}
