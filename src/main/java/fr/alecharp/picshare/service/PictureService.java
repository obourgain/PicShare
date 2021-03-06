package fr.alecharp.picshare.service;

import com.google.inject.Singleton;
import fr.alecharp.picshare.domain.Picture;
import net.codestory.http.Part;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;
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
    private final String storage;
    private final String prefix;

    @Inject
    public PictureService(@Named("storage-location") String storage,
                          @Named("picture-prefix") String prefix) {
        this.storage = storage;
        this.prefix = prefix;
    }

    public Optional<Picture> upload(String eventID, Part part) {
        try {
            Path target = Paths.get(storage, eventID, part.fileName());
            Files.copy(part.inputStream(), target);
            String thumb = createThumbForPicture(target, Paths.get(storage, eventID, ".thumb", part.fileName()));
            return Optional.of(new Picture(part.fileName(), target.toString().replace(storage, prefix), thumb));
        } catch (IOException e) {
            // TODO: log
            return Optional.empty();
        }
    }

    private String createThumbForPicture(Path src, Path dest) throws IOException {
        BufferedImage original = ImageIO.read(src.toFile());
        BufferedImage resize = Scalr.resize(original, Scalr.Method.SPEED, 640, 480);
        ImageIO.write(resize, "jpg", dest.toFile());
        return dest.toString().replace(storage, prefix);
    }

    public Path get(String eventId, String pictureTitle) {
        return Paths.get(storage, eventId, pictureTitle);
    }

    public Path get(Picture picture) {
        return Paths.get(picture.path().replace(prefix, storage));
    }
}
