package org.saartako.client.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import org.saartako.playlist.Playlist;

public class PlaylistCard extends Control {

    private final ObjectProperty<Playlist> playlist = new SimpleObjectProperty<>(this, "playlist");

    @Override
    protected PlaylistCardSkin createDefaultSkin() {
        return new PlaylistCardSkin(this);
    }

    public ObjectProperty<Playlist> playlistProperty() {
        return this.playlist;
    }

    public Playlist getPlaylist() {
        return this.playlist.get();
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist.set(playlist);
    }
}
