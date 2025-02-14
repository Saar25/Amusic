package org.saartako.client.controls;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.saartako.playlist.Playlist;

public class PlaylistsGrid extends Control {

    private final ListProperty<Playlist> playlists = new SimpleListProperty<>(this, "playlists");

    private final StringProperty filter = new SimpleStringProperty(this, "filter");

    // TODO: move filtered playlists logic here

    @Override
    protected PlaylistsGridSkin createDefaultSkin() {
        return new PlaylistsGridSkin(this);
    }

    public ListProperty<Playlist> playlistsProperty() {
        return this.playlists;
    }

    public ObservableList<Playlist> getPlaylists() {
        return this.playlists.get();
    }

    public StringProperty filterProperty() {
        return this.filter;
    }

    public String getFilter() {
        return this.filter.get();
    }
}
