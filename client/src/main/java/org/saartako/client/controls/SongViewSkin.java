package org.saartako.client.controls;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import org.saartako.client.models.CardItem;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.SongUtils;
import org.saartako.common.song.Song;

public class SongViewSkin extends SkinBase<SongView> {

    private final SongService songService = SongService.getInstance();

    private final MusicCard musicCard = new MusicCard();

    private final VBox node = new VBox(16);

    public SongViewSkin(SongView control) {
        super(control);

        this.node.getChildren().setAll(this.musicCard);

        registerChangeListener(songService.currentSongProperty(), observable ->
            updateSong(this.songService.getCurrentSong()));
        updateSong(this.songService.getCurrentSong());

        getChildren().setAll(this.node);
    }

    private void updateSong(Song song) {
        final CardItem cardItem = SongUtils.songToCardItem(song);

        this.musicCard.setCardItem(cardItem);
    }
}
