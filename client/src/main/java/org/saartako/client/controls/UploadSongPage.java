package org.saartako.client.controls;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.FileChooser;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.GenreService;
import org.saartako.client.services.LanguageService;
import org.saartako.client.services.SongService;
import org.saartako.common.genre.Genre;
import org.saartako.common.language.Language;
import org.saartako.common.song.CreateSongDTO;

import java.io.File;

public class UploadSongPage extends Control implements RouteNode {

    private final SongService songService = SongService.getInstance();
    private final LanguageService languageService = LanguageService.getInstance();
    private final GenreService genreService = GenreService.getInstance();

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

    public ObjectProperty<String> songNameProperty() {
        return this.songName;
    }

    public ObjectProperty<Genre> genreProperty() {
        return this.genre;
    }

    public ObjectProperty<Language> languageProperty() {
        return this.language;
    }

    public ListProperty<Language> languagesProperty() {
        return this.languageService.languagesProperty();
    }

    public ListProperty<Genre> genresProperty() {
        return this.genreService.genresProperty();
    }

    public void onSaveSongButtonClick() {
        // TODO: upload audio
        final CreateSongDTO createSong = new CreateSongDTO(
            this.songName.get(),
            this.genre.get() == null ? null : this.genre.get().getId(),
            this.language.get() == null ? null : this.language.get().getId()
        );
        this.songService.createSong(createSong)
            .whenComplete((songDTO, throwable) -> {
                Platform.runLater(() -> {
                    final Alert alert;
                    if (throwable != null) {
                        alert = new Alert(Alert.AlertType.ERROR, "Failed to save song\n" + throwable.getMessage());
                    } else {
                        alert = new Alert(Alert.AlertType.INFORMATION, "Succeeded to save song");
                        clearForm();
                    }
                    alert.show();
                });
            })
            .thenCompose(song -> this.songService.uploadSongAudioFile(song, this.audioFile.get()))
            .whenComplete((fileName, throwable) -> {
                Platform.runLater(() -> {
                    final Alert alert = throwable != null
                        ? new Alert(Alert.AlertType.ERROR, "Failed to upload audio file\n" + throwable.getMessage())
                        : new Alert(Alert.AlertType.INFORMATION, "Succeeded to upload audio file");
                    alert.show();
                });
            });
    }

    public void onUploadButtonClick() {
        final FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(getScene().getWindow());
        this.audioFile.set(file);
    }

    private void clearForm() {
        this.audioFile.set(null);
        this.songName.set(null);
        this.genre.set(null);
        this.language.set(null);
    }
}
