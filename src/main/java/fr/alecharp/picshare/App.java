package fr.alecharp.picshare;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import fr.alecharp.picshare.config.DataBaseModule;
import fr.alecharp.picshare.config.PicShareModule;
import fr.alecharp.picshare.http.EventController;
import fr.alecharp.picshare.resource.EventResource;
import net.codestory.http.WebServer;
import net.codestory.http.injection.GuiceAdapter;
import org.dalesbred.Database;
import org.flywaydb.core.Flyway;

import java.nio.file.Paths;

/**
 * @author Adrien Lecharpentier
 */
public class App {
    public static void main(String[] args) {
        final Injector injector = Guice.createInjector(new PicShareModule(), new DataBaseModule());

        Flyway flyway = new Flyway();
        String url = injector.getInstance(Key.get(String.class, Names.named("jdbc.url")));
        String user = injector.getInstance(Key.get(String.class, Names.named("jdbc.user")));
        String password = injector.getInstance(Key.get(String.class, Names.named("jdbc.password")));

        flyway.setDataSource(url, user, password);
        flyway.migrate();

        new WebServer()
            .configure(r -> {
                String storage = injector.getInstance(Key.get(String.class, Names.named("storage-location")));
                String prefix = injector.getInstance(Key.get(String.class, Names.named("picture-prefix")));
                r
                    .setIocAdapter(new GuiceAdapter(injector))
                    .get("/ping", "pong")
                    .add(EventController.class)
                    .add(EventResource.class)
                    .bind(prefix, Paths.get(storage).toFile());
            }).start(8080);
    }
}
