package fr.alecharp.picshare.repository;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.dalesbred.Database;
import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.domain.Picture;

/**
 * @author Adrien Lecharpentier
 */
@Singleton
public class EventRepository {
    private final Database db;

    @Inject
    public EventRepository(Database db) {
        this.db = db;
    }

    public Optional<Event> save(Event event) {
        return Optional.ofNullable(db.withTransaction(tx -> {
            int res = db.update("INSERT INTO events (id, title, date) VALUES(?,?,?)", event.id(), event.title(), event.date());
            if (res == 1) {
                updatePictures(event.pictures().stream());
                updateEventPictureMappings(event, event.pictures().stream());
                return event;
            } else {
                tx.setRollbackOnly();
                return null;
            }
        }));
    }

    public Optional<Event> update(Event event) {
        return Optional.ofNullable(db.withTransaction(tx -> {
            int res = db.update("UPDATE events SET title = ?, date = ? WHERE id = ?", event.title(), event.date(), event.id());
            if (res == 1) {
                // TODO: filter pictures already stored in DB
                Set<Picture> picturesAlreadyPresent = findPicture(event);
                Stream<Picture> pictureToInsert = event.pictures().stream()
                        .filter(pic -> !picturesAlreadyPresent.contains(pic));
                updatePictures(pictureToInsert);
                updateEventPictureMappings(event, pictureToInsert);
                return event;
            } else {
                tx.setRollbackOnly();
                return null;
            }
        }));
    }

    public Optional<Event> findById(String id) {
        return db.withTransaction(tx -> {
            Optional<Event> optional = db.findOptional(Event.class, "SELECT * FROM events WHERE id = ?", id);
            return optional.map(event ->  {
                Set<Picture> pictures =
                        db.findAll(String.class, "SELECT id_picture FROM event_picture WHERE id_event = ?", event.id())
                                .stream()
                                .map(pictureId -> db.findOptional(Picture.class, "SELECT * FROM pictures WHERE id = ?", pictureId))
                                .filter(Optional::isPresent).map(Optional::get)
                                .collect(toSet());
                event.pictures(pictures);
                return event;
            });
        });
    }

    private Set<Picture> findPicture(Event event) {
        return event.pictures().stream()
                .filter(pic -> !db.findOptional(Picture.class, "SELECT * FROM pictures WHERE id = ?", pic.id()).isPresent())
                .collect(toSet());
    }

    private void updateEventPictureMappings(Event event, Stream<Picture> pictureStream) {
        db.updateBatch("INSERT INTO event_picture(id_event, id_picture) VALUES(?,?)",
                pictureStream
                        .map(pic -> asList(event.id(), pic.id()))
                        .collect(toList()));
    }

    private void updatePictures(Stream<Picture> pictureStream) {
        db.updateBatch("INSERT INTO pictures(id, title, path, thumb) VALUES(?,?,?,?)",
                pictureStream
                        .map(pic -> asList(pic.id(), pic.title(), pic.path(), pic.thumb()))
                        .collect(toList()));
    }

}
