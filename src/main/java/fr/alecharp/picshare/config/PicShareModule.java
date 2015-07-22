package fr.alecharp.picshare.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author Adrien Lecharpentier
 */
public class PicShareModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    @Named("storage-location")
    public String provideStorageLocation() {
        return "pictures";
    }

    @Provides
    @Singleton
    @Named("picture-prefix")
    public String providePicturePrefix() {
        return "/pictures";
    }
}
