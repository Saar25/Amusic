package org.saartako.client.controls;

import javafx.beans.property.ListProperty;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import org.saartako.client.services.SongService;
import org.saartako.song.Song;

public class SongsPageSkin implements Skin<SongsPage> {

    private final SongsPage control;

    private final SongsGrid node = new SongsGrid();

    public SongsPageSkin(SongsPage control) {
        this.control = control;

        final SongService songService = SongService.getInstance();
        final ListProperty<Song> observable = songService.songsProperty();
        this.node.songsProperty().bind(observable);
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
    }
}
