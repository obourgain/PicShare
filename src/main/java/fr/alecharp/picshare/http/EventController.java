package fr.alecharp.picshare.http;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.domain.Picture;
import fr.alecharp.picshare.service.EventService;
import fr.alecharp.picshare.service.PictureService;
import net.codestory.http.Request;
import net.codestory.http.annotations.Post;
import net.codestory.http.annotations.Prefix;
import net.codestory.http.annotations.Put;
import net.codestory.http.payload.Payload;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * @author Adrien Lecharpentier
 */
@Prefix("/api/event")
public class EventController {
    private final EventService eventService;
    private final PictureService pictureService;

    @Inject
    public EventController(EventService eventService, PictureService pictureService) {
        this.eventService = eventService;
        this.pictureService = pictureService;
    }

    @Post
    public Payload create(Event event) {
        Optional<Event> save = eventService.save(event);
        return save.isPresent() ? Payload.created("/api/event/" + save.get().id()) :
                Payload.badRequest();
    }

    @Put("/:id/pictures")
    public Payload attachPicture(String id, Request req) {
        Set<Picture> pictures = req.parts().stream()
                .filter(part -> part.name().equals("picture"))
                .map(part -> pictureService.upload(id, part))
                .filter(Optional::isPresent).map(Optional::get)
                .collect(toSet());
        Optional<Event> event = eventService.attachPictures(id, pictures);
        if (!event.isPresent()) {
            return Payload.notFound();
        }
        return Payload.ok().withHeader("Location", "/api/event/" + event.get().id());
    }
}
