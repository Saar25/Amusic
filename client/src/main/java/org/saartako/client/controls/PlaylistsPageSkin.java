package org.saartako.client.controls;

import javafx.beans.property.ListProperty;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import org.saartako.client.services.PlaylistService;
import org.saartako.playlist.Playlist;

public class PlaylistsPageSkin implements Skin<PlaylistsPage> {

    private final PlaylistsPage control;

    private final PlaylistsGrid node = new PlaylistsGrid();

    public PlaylistsPageSkin(PlaylistsPage control) {
        this.control = control;

        final PlaylistService playlistService = PlaylistService.getInstance();
        final ListProperty<Playlist> observable = playlistService.playlistsProperty();
        this.node.playlistsProperty().bind(observable);
    }

    @Override
    public PlaylistsPage getSkinnable() {
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
