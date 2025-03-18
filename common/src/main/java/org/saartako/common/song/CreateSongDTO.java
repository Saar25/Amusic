package org.saartako.common.song;

public record CreateSongDTO(String name, Long genreId, Long languageId, String mediaType) {
}
