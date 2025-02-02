package org.saartako.client.services;

import org.saartako.song.Song;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

public class SongService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static SongService INSTANCE;

    private final HttpService httpService;

    public SongService(HttpService httpService) {
        this.httpService = httpService;
    }

    public static synchronized SongService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SongService(HttpService.getInstance());
        }
        return INSTANCE;
    }

    public CompletableFuture<Song[]> fetchSongs() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                LOGGER.info("Trying to fetch songs");
                final Song[] songs = this.httpService.fetchSongs();
                LOGGER.info("Fetch songs successfully");

                return songs;
            } catch (IOException | InterruptedException e) {
                LOGGER.info("Failed fetch songs - {}", e.getMessage());

                throw new RuntimeException(e);
            }
        });
    }
}
