package fr.alecharp.picshare.service;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.domain.Picture;
import fr.alecharp.picshare.repository.EventRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author Adrien Lecharpentier
 */
@Singleton
public class EventService {
    private final EventRepository eventRepository;
    private final PictureService pictureService;
    private final String storage;

    @Inject
    public EventService(EventRepository eventRepository,
                        @Named("storage-location") String storage,
                        PictureService pictureService) {
        this.eventRepository = eventRepository;
        this.storage = storage;
        this.pictureService = pictureService;
    }

    public Optional<Event> save(Event event) {
        try {
            Optional<Event> saved = eventRepository.save(event);
            if (saved.isPresent()) {
                Files.createDirectories(Paths.get(storage, event.id(), ".thumb"));
            }
            return saved;
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public Optional<Event> get(String id) {
        return eventRepository.findById(id);
    }

    public Optional<Event> attachPictures(String id, Set<Picture> pictures) {
        Optional<Event> event = get(id);
        if (!event.isPresent()) return event;
        return eventRepository.update(event.get().pictures(pictures));
    }

    public void generateZip(Event event, OutputStream stream) throws IOException {
        try (ZipOutputStream zip = new ZipOutputStream(stream)) {
            for (Picture picture : event.pictures()) {
                zip.putNextEntry(new ZipEntry(picture.title()));
                Files.copy(pictureService.get(picture), zip);
                zip.closeEntry();
            }
        }
    }
}
