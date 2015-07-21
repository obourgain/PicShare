package fr.alecharp.picshare.http;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.service.EventService;
import net.codestory.http.annotations.Post;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Prefix("/api/event")
public class EventController {
    private final EventService eventService;

    @Inject
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @Post
    public Payload create(Event event) {
        Optional<Event> save = eventService.save(event);
        return save.isPresent() ? Payload.created("/api/event/" + save.get().id()) :
                Payload.badRequest();
    }
}
