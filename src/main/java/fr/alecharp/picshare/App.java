package fr.alecharp.picshare;

import net.codestory.http.WebServer;

/**
 * @author Adrien Lecharpentier
 */
public class App {
    public static void main(String[] args) {
        new WebServer()
                .configure(r -> r.get("/ping", "pong"))
                .start(8080);
    }
}
