package fr.alecharp.picshare;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import fr.alecharp.picshare.config.PicShareModule;
import fr.alecharp.picshare.http.EventController;
import fr.alecharp.picshare.resource.EventResource;
import net.codestory.http.WebServer;
import net.codestory.http.injection.GuiceAdapter;

import java.nio.file.Paths;

/**
 * @author Adrien Lecharpentier
 */
public class App {
    public static void main(String[] args) {
        new WebServer()
            .configure(r -> {
                Injector injector = Guice.createInjector(new PicShareModule());
                String storageLocation = injector.getInstance(Key.get(String.class, Names.named("storageLocation")));
                r
                    .setIocAdapter(new GuiceAdapter(injector))
                    .get("/ping", "pong")
                    .add(EventController.class)
                    .add(EventResource.class)
                    .bind(String.format("/%s/", storageLocation), Paths.get(storageLocation).toFile());
            }).start(8080);
    }
}
