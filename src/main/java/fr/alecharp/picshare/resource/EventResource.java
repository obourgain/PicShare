package fr.alecharp.picshare.resource;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.service.EventService;
import fr.alecharp.picshare.service.PictureService;
import net.codestory.http.Response;
import net.codestory.http.annotations.Get;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.templating.Model;
import net.codestory.http.templating.ModelAndView;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Prefix("/event")
public class EventResource {
    private final EventService eventService;
    private final PictureService pictureService;

    @Inject
    public EventResource(EventService eventService, PictureService pictureService) {
        this.eventService = eventService;
        this.pictureService = pictureService;
    }

    @Get("/:id")
    public ModelAndView dashboard(String id) {
        Optional<Event> event = eventService.get(id);
        return event.isPresent() ?
                ModelAndView.of("event/display.html", "event", event.get()) :
                ModelAndView.of("404.html");
    }

    @Get("/:id/:picture")
    public void download(String id, String picture, Response resp) throws IOException {
        resp.setHeader("Content-Type", "application/octet-stream");
        Files.copy(pictureService.getPicture(id, picture), resp.outputStream());
    }
}
