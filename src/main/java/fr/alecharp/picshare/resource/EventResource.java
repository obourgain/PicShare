package fr.alecharp.picshare.resource;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.service.EventService;
import net.codestory.http.annotations.Get;
import net.codestory.http.templating.Model;
import net.codestory.http.templating.ModelAndView;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
public class EventResource {
    private final EventService eventService;

    @Inject
    public EventResource(EventService eventService) {
        this.eventService = eventService;
    }

    @Get("/event/:id")
    public ModelAndView dashboard(String id) {
        Optional<Event> event = eventService.get(id);
        return event.isPresent() ?
                ModelAndView.of("event/display.html", "event", event.get()) :
                ModelAndView.of("404.html");
    }
}
