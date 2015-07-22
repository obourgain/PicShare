package fr.alecharp.picshare.domain;


import java.time.LocalDate;
import java.util.*;

/**
 * @author Adrien Lecharpentier
 */
public class Event {
    private String id;

    private String title;
    private LocalDate date;
    private Set<Picture> pictures = new TreeSet<>(
            (p1, p2) -> p1.title().compareTo(p2.title())
    );

    private Event() {
        this(null, null);
    }

    public Event(String title, LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.date = date;
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public LocalDate date() {
        return date;
    }

    public Set<Picture> pictures() {
        return Collections.unmodifiableSet(pictures);
    }

    public Event pictures(Set<Picture> pictures) {
        this.pictures.addAll(pictures);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(title, event.title) &&
                Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
