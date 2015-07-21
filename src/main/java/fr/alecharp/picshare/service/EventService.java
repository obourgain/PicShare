package fr.alecharp.picshare.service;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.repository.EventRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Singleton
public class EventService {
    private final EventRepository eventRepository;

    @Inject
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Event> save(Event event) {
        event = eventRepository.save(event);
        return Optional.ofNullable(event);
    }

    public void remove(String id) {
        eventRepository.delete(id);
    }

    public Optional<Event> get(String id) {
        return Optional.ofNullable(eventRepository.findById(id));
    }
}
