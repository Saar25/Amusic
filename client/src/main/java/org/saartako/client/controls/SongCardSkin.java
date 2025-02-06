package org.saartako.client.controls;

import atlantafx.base.controls.Card;
import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.saartako.song.Song;

public class SongCardSkin implements Skin<SongCard> {

    private final SongCard control;

    private final Card node = new Card();

    private final Tile headerTile = new Tile();

    private final VBox footer = new VBox();

    private final ChangeListener<Song> footerLabelsListener;

    public SongCardSkin(SongCard control) {
        this.control = control;

        this.node.getStyleClass().add(Styles.ELEVATED_4);

        final Circle headerGraphic = new Circle(8, Color.rgb(
            (int) (Math.random() * 256),
            (int) (Math.random() * 256),
            (int) (Math.random() * 256)));
        this.headerTile.setGraphic(headerGraphic);
        this.headerTile.titleProperty().bind(Bindings.createStringBinding(
            () -> this.control.songProperty().getValue().getName(),
            this.control.songProperty()));
        this.node.setHeader(this.headerTile);

        this.footerLabelsListener = (observable, oldValue, newValue) -> onSongChange(newValue);
        this.control.songProperty().addListener(this.footerLabelsListener);

        onSongChange(this.control.getSong());

        this.node.setFooter(this.footer);
    }

    private void onSongChange(Song song) {
        this.footer.getChildren().clear();

        if (song.getUploader() != null) {
            final String value = song.getUploader().getDisplayName();
            this.footer.getChildren().add(new Label("By: " + value));
        }
        if (song.getGenre() != null) {
            final String value = song.getGenre().getName();
            this.footer.getChildren().add(new Label("Genre: " + value));
        }
        if (song.getLanguage() != null) {
            final String value = song.getLanguage().getName();
            this.footer.getChildren().add(new Label("Language: " + value));
        }
    }

    @Override
    public SongCard getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
        this.headerTile.titleProperty().unbind();
        this.control.songProperty().removeListener(this.footerLabelsListener);
    }
}
