package org.saartako.genre;

public class GenreDTO implements Genre {

    private long id;
    private String name;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
}
