package fr.alecharp.picshare.repository;

import fr.alecharp.picshare.domain.Event;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Singleton
public class EventRepository {
    private final Map<String, Event> eventStore = new HashMap<>();

    public Event save(Event event) {
        eventStore.put(event.id(), event);
        return event;
    }

    public void delete(String id) {
        eventStore.remove(id);
    }

    public Event findById(String id) {
        return eventStore.get(id);
    }
}
