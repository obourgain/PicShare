package fr.alecharp.picshare.service;

import com.google.inject.Singleton;
import fr.alecharp.picshare.domain.Picture;
import net.codestory.http.Part;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Singleton
public class PictureService {
    private final String storageLocation;

    @Inject
    public PictureService(@Named("storageLocation") String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Optional<Picture> upload(String eventID, Part part) {
        try {
            Path target = Paths.get(storageLocation, eventID, part.fileName());
            Files.copy(part.inputStream(), target);
            return Optional.of(new Picture(part.fileName(), "/" + target.toString()));
        } catch (IOException e) {
            // TODO: log
            return Optional.empty();
        }
    }
}
