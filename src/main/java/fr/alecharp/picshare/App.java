package fr.alecharp.picshare;

import fr.alecharp.picshare.config.PicShareModule;
import net.codestory.http.WebServer;
import net.codestory.http.injection.GuiceAdapter;

/**
 * @author Adrien Lecharpentier
 */
public class App {
    public static void main(String[] args) {
        new WebServer()
            .configure(r -> r
                .setIocAdapter(new GuiceAdapter(new PicShareModule()))
                .get("/ping", "pong")
            ).start(8080);
    }
}
