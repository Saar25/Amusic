package org.saartako.common.genre;

public class GenreUtils {

    private GenreUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static GenreDTO copy(Genre genre) {
        return genre == null
            ? null
            : new GenreDTO()
                .setId(genre.getId())
                .setName(genre.getName());
    }

    public static String toString(Genre genre) {
        return "Genre{" +
               "id=" + genre.getId() +
               ", name='" + genre.getName() + '\'' +
               '}';
    }
}
