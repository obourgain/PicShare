package fr.alecharp.picshare.service;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.domain.Picture;
import fr.alecharp.picshare.repository.EventRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

/**
 * @author Adrien Lecharpentier
 */
@Singleton
public class EventService {
    private final EventRepository eventRepository;
    private final String storage;

    @Inject
    public EventService(EventRepository eventRepository, @Named("storageLocation") String storage) {
        this.eventRepository = eventRepository;
        this.storage = storage;
    }

    public Optional<Event> save(Event event) {
        try {
            event = eventRepository.save(event);
            Files.createDirectories(Paths.get(storage, event.id(), ".thumb"));
            return Optional.ofNullable(event);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void remove(String id) {
        eventRepository.delete(id);
    }

    public Optional<Event> get(String id) {
        return Optional.ofNullable(eventRepository.findById(id));
    }

    public Optional<Event> attachPictures(String id, Set<Picture> pictures) {
        Optional<Event> event = get(id);
        if (!event.isPresent()) return event;
        return save(event.get().pictures(pictures));
    }
}
