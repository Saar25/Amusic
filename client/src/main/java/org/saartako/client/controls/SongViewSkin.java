package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.VBox;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.song.Song;

public class SongViewSkin implements Skin<SongView> {

    private final SongService songService = SongService.getInstance();

    private final SongView control;

    private final MusicCard musicCard = new MusicCard();

    private final VBox node = new VBox(16);

    public SongViewSkin(SongView control) {
        this.control = control;

        this.node.getChildren().setAll(musicCard);

        this.control.songProperty().addListener((observable, oldValue, newValue) ->
            updateSong(newValue));

        updateSong(this.control.getSong());
    }

    private void updateSong(Song song) {
        final CardItem cardItem = SongUtils.songToCardItem(song);

        this.musicCard.setCardItem(cardItem);
    }

    @Override
    public SongView getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
    }
}
