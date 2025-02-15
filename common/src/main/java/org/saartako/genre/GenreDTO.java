package org.saartako.genre;

public class GenreDTO implements Genre {

    private long id;
    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    public GenreDTO setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public GenreDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return GenreUtils.toString(this);
    }
}
