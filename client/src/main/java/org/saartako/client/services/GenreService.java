package org.saartako.client.services;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import org.saartako.common.genre.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class GenreService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final AtomicBoolean isDataValid = new AtomicBoolean(false);

    private final GenreApiService genreApiService;
    private final AuthService authService;

    private final ListProperty<Genre> genres = new SimpleListProperty<>(this, "genres");

    private GenreService(GenreApiService genreApiService, AuthService authService) {
        this.genreApiService = genreApiService;
        this.authService = authService;
    }

    public static GenreService getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public ListProperty<Genre> genresProperty() {
        return this.genres;
    }

    public void fetchData() {
        if (this.isDataValid.compareAndSet(false, true)) {
            if (!this.authService.isLoggedIn()) {
                this.genres.setValue(FXCollections.observableArrayList());
            } else {
                fetchGenres().whenComplete((genres, error) -> {
                    if (error != null) {
                        Platform.runLater(() -> {
                            final Alert alert = new Alert(
                                Alert.AlertType.INFORMATION,
                                "Failed to fetch genres\n" + error.getMessage());
                            alert.show();
                        });
                    }
                });
            }
        }
    }

    public CompletableFuture<Genre[]> fetchGenres() {
        LOGGER.info("Trying to fetch genres");

        return this.genreApiService.fetchGenres()
            .whenComplete((genres, throwable) -> {
                if (throwable != null) {
                    LOGGER.error("Failed to fetch genres - {}", throwable.getMessage());

                    this.genres.setValue(FXCollections.observableArrayList());
                } else {
                    LOGGER.info("Succeeded to fetch genres");

                    this.genres.setValue(FXCollections.observableArrayList(genres));
                }
            });
    }

    private static final class InstanceHolder {
        private static final GenreService INSTANCE = new GenreService(
            GenreApiService.getInstance(),
            AuthService.getInstance());
    }
}
