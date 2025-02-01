package org.saartako.genre;

public class GenreUtils {

    private GenreUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static String toString(Genre genre) {
        return "Genre{" +
               "id=" + genre.getId() +
               ", name='" + genre.getName() + '\'' +
               '}';
    }
}
