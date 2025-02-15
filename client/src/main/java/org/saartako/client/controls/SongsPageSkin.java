package org.saartako.client.controls;

import atlantafx.base.controls.CustomTextField;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.ColorUtils;
import org.saartako.song.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SongsPageSkin implements Skin<SongsPage> {

    private final SongService songService = SongService.getInstance();

    private final SongsPage control;

    private final ChangeListener<ObservableList<Song>> songsChangeListener;

    private final CustomTextField searchTextField = new CustomTextField();
    private final MusicCardGrid musicCardGrid = new MusicCardGrid();
    private final Label loadingLabel = new Label("Loading...");

    private final VBox node = new VBox(16);

    public SongsPageSkin(SongsPage control) {
        this.control = control;

        this.node.setAlignment(Pos.TOP_CENTER);
        this.node.setPadding(new Insets(8, 40, 8, 40));

        this.searchTextField.setPromptText("Search");
        this.searchTextField.setMaxWidth(300);
        this.searchTextField.setLeft(new FontIcon(Material2MZ.SEARCH));
        this.searchTextField.setRight(new FontIcon(Material2AL.CLEAR));
        this.node.getChildren().add(this.searchTextField);

        this.loadingLabel.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLDER);
        this.node.getChildren().add(this.loadingLabel);

        this.searchTextField.textProperty().addListener((o, prev, search) ->
            updateSongs(this.songService.getSongs(), search));
        this.songsChangeListener = (o, prev, songs) ->
            updateSongs(songs, this.searchTextField.getText());
        this.songService.songsProperty().addListener(this.songsChangeListener);

        updateSongs(this.songService.getSongs(), this.searchTextField.getText());
    }

    private void updateSongs(ObservableList<Song> songs, String search) {
        if (songs == null) {
            this.node.getChildren().set(1, this.loadingLabel);
        } else {
            final List<? extends Song> filtered = this.songService.filterSongs(songs, search);

            final List<CardItem> cardItems = new ArrayList<>(filtered.stream().map(song -> {
                final Map<String, String> details = new TreeMap<>();
                if (song.getUploader() != null) {
                    details.put("By", song.getUploader().getDisplayName());
                }
                if (song.getGenre() != null) {
                    details.put("Genre", song.getGenre().getName());
                }
                if (song.getLanguage() != null) {
                    details.put("Language", song.getLanguage().getName());
                }

                final Paint songColor = ColorUtils.getSongColor(song);

                return new CardItem(song.getName(), details, songColor);
            }).toList());

            Platform.runLater(() -> {
                this.musicCardGrid.cardItemsProperty().setAll(cardItems);
                this.node.getChildren().set(1, this.musicCardGrid);
            });
        }
    }

    @Override
    public SongsPage getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
        this.songService.songsProperty().removeListener(this.songsChangeListener);
    }
}
