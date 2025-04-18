package org.saartako.server.genre;

import jakarta.persistence.*;
import org.saartako.common.genre.Genre;
import org.saartako.common.genre.GenreUtils;

@Entity(name = "genres")
public class GenreEntity implements Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16)
    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return GenreUtils.toString(this);
    }
}
