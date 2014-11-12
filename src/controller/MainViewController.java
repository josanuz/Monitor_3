package controller;

import entities.Server;

/**
 * Created by Casa on 12/11/2014.
 */
public class MainViewController {
    private static Server actualSelectedServer;

    public static Server actualServer() {
        return actualSelectedServer;
    }
}
