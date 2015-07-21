package fr.alecharp.picshare.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Adrien Lecharpentier
 */
public class Picture {
    private String id;

    private String title;
    private String path;
    private String thumb;

    private Picture() {
        this(null, null, null);
    }

    public Picture(String title, String path, String thumb) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.path = path;
        this.thumb = thumb;
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String path() {
        return path;
    }

    public String thumb() {
        return thumb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(path, picture.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }
}
