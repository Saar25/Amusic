package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.GenreService;
import org.saartako.client.services.LanguageService;
import org.saartako.client.services.SongService;
import org.saartako.common.genre.Genre;
import org.saartako.common.language.Language;
import org.saartako.common.song.CreateSongDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

public class UploadSongPage extends Control implements RouteNode {

    private final SongService songService = SongService.getInstance();
    private final LanguageService languageService = LanguageService.getInstance();
    private final GenreService genreService = GenreService.getInstance();

    private final ObjectProperty<String> mediaType = new SimpleObjectProperty<>(this, "mediaType", null);
    private final ObjectProperty<File> audioFile = new SimpleObjectProperty<>(this, "audioFile", null);
    private final ObjectProperty<String> songName = new SimpleObjectProperty<>(this, "songName", null);
    private final ObjectProperty<Genre> genre = new SimpleObjectProperty<>(this, "genre", null);
    private final ObjectProperty<Language> language = new SimpleObjectProperty<>(this, "language", null);

    @Override
    protected UploadSongPageSkin createDefaultSkin() {
        return new UploadSongPageSkin(this);
    }

    @Override
    public void onEnterView() {
        this.languageService.fetchData();
        this.genreService.fetchData();
    }

    public ObjectProperty<File> audioFileProperty() {
        return this.audioFile;
    }

    public ObjectProperty<String> songNameProperty() {
        return this.songName;
    }

    public ObjectProperty<Genre> genreProperty() {
        return this.genre;
    }

    public ObjectProperty<Language> languageProperty() {
        return this.language;
    }

    public ListProperty<Genre> genresProperty() {
        return this.genreService.genresProperty();
    }

    public ListProperty<Language> languagesProperty() {
        return this.languageService.languagesProperty();
    }

    public void onSaveSongButtonClick() {
        if (this.songName.get() == null) {
            final Alert alert = new Alert(Alert.AlertType.ERROR,
                "Please fill the file name");
            alert.show();
            return;
        }

        final CompletableFuture<Media> mediaIsReadyFuture = new CompletableFuture<>();

        if (this.audioFile.get() != null) {
            final String audioFilePath = this.audioFile.get().toPath().toUri().toString();
            final Media media = new Media(audioFilePath);
            final MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnReady(() -> mediaIsReadyFuture.complete(media));
        } else {
            mediaIsReadyFuture.complete(null);
        }

        mediaIsReadyFuture.thenCompose(media -> {
            final Long millis = media == null ? null : (long) media.getDuration().toMillis();

            final CreateSongDTO createSong = new CreateSongDTO(
                this.songName.get(),
                this.genre.get() == null ? null : this.genre.get().getId(),
                this.language.get() == null ? null : this.language.get().getId(),
                this.mediaType.get(),
                millis
            );

            return this.songService.uploadSong(createSong, this.audioFile.get());
        }).whenComplete((songDTO, throwable) -> {
            Platform.runLater(() -> {
                final Alert alert;
                if (throwable != null) {
                    alert = new Alert(Alert.AlertType.ERROR, "Failed to upload song\n" + throwable.getMessage());
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to upload song");
                    clearForm();
                }
                alert.show();
            });
        });
    }

    public void onUploadButtonClick() {
        final FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(getScene().getWindow());

        String mediaType = null;
        try {
            mediaType = Files.probeContentType(file.toPath());
        } catch (IOException ignored) {
        }

        if (mediaType == null) {
            final Alert alert = new Alert(Alert.AlertType.ERROR,
                "Failed uploading song,\nCannot find media type of audio file");
            alert.show();
            this.mediaType.set(null);
            this.audioFile.set(null);
        } else {
            this.mediaType.set(mediaType);
            this.audioFile.set(file);
        }
    }

    private void clearForm() {
        this.audioFile.set(null);
        this.songName.set(null);
        this.genre.set(null);
        this.language.set(null);
    }
}
