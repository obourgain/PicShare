package fr.alecharp.picshare.repository;

import fr.alecharp.picshare.domain.Event;
import fr.alecharp.picshare.domain.Picture;
import org.dalesbred.Database;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.*;

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
                event.pictures().forEach(pic -> {
                    db.update("INSERT INTO pictures(id, title, path, thumb) VALUES(?,?,?,?)", pic.id(), pic.title(), pic.path(), pic.thumb());
                    db.update("INSERT INTO event_picture(id_event, id_picture) VALUES(?,?)", event.id(), pic.id());
                });
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
                event.pictures().stream()
                        // TODO: filter pictures already stored in DB
                        .filter(pic ->
                            !db.findOptional(Picture.class, "SELECT * FROM pictures WHERE id = ?", pic.id()).isPresent()
                        )
                        .forEach(pic -> {
                            db.update("INSERT INTO pictures(id, title, path, thumb) VALUES(?,?,?,?)", pic.id(), pic.title(), pic.path(), pic.thumb());
                            db.update("INSERT INTO event_picture(id_event, id_picture) VALUES(?,?)", event.id(), pic.id());
                        });
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
}
