package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.paint.Paint;
import org.saartako.client.models.CardItem;
import org.saartako.client.utils.SongUtils;
import org.saartako.song.Song;

import java.util.Map;
import java.util.TreeMap;

public class SongCardSkin implements Skin<SongCard> {

    private final SongCard control;

    private final MusicCard node = new MusicCard();

    public SongCardSkin(SongCard control) {
        this.control = control;

        this.control.songProperty().addListener(
            (o, prev, song) -> updateSong(song));
        updateSong(this.control.getSong());
    }

    private void updateSong(Song song) {
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

        final Paint songColor = SongUtils.getSongColor(song);

        this.node.setCardItem(new CardItem(song.getName(), details, songColor));
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
    }
}
