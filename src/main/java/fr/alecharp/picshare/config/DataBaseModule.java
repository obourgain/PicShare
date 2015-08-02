package fr.alecharp.picshare.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import org.dalesbred.Database;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Adrien Lecharpentier
 */
public class DataBaseModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/picshare.properties");
            Properties props = new Properties();
            props.load(resourceAsStream);
            Names.bindProperties(binder(), props);
        } catch (IOException e) {
            // TODO: log
            e.printStackTrace();
        }
    }

    @Provides
    @Singleton
    @Inject
    public Database provideConnectionToDatabase(@Named("jdbc.url") String url,
                                                @Named("jdbc.user") String user,
                                                @Named("jdbc.password") String password) throws SQLException {
        return Database.forUrlAndCredentials(url, user, password);
    }
}
