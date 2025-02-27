package org.saartako.common.song;

public record CreateSongDTO(String name, Long genreId, Long languageId) {
    public CreateSongDTO(String name) {
        this(name, null, null);
    }
}
