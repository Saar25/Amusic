package org.saartako.common.genre;

import java.util.Collection;
import java.util.List;

public class GenreUtils {

    private GenreUtils() {
        throw new RuntimeException("Cannot create instance of class " + getClass().getName());
    }

    public static List<GenreDTO> copy(Collection<? extends Genre> genre) {
        return genre.stream().map(GenreUtils::copy).toList();
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
