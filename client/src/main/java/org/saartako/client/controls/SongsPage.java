package org.saartako.client.controls;

import javafx.beans.binding.ListBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.saartako.client.enums.Route;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.BindingsUtils;
import org.saartako.common.song.Song;

public class SongsPage extends Control implements RouteNode {

    private final SongService songService = SongService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final ObjectProperty<String> songsFilter = new SimpleObjectProperty<>(this, "songsFilter", "");

    private final ListBinding<Song> filteredSongs = BindingsUtils.createJavaListBinding(() -> {
        final ObservableList<Song> songs = this.songService.songsProperty();
        if (songs == null) return null;
        final String filter = this.songsFilter.get();
        if (filter == null) return songs;
        return this.songService.filterSongs(songs, filter);
    }, this.songService.songsProperty(), this.songsFilter);

    @Override
    protected SongsPageSkin createDefaultSkin() {
        return new SongsPageSkin(this);
    }

    @Override
    public void onEnterView() {
        this.songService.fetchData();
    }

    public ObjectProperty<String> songsFilterProperty() {
        return this.songsFilter;
    }

    public ListBinding<Song> filteredSongsProperty() {
        return this.filteredSongs;
    }

    public void onExpandSong(Song song) {
        this.songService.setCurrentSong(song);
        this.routerService.push(Route.SONG_VIEW);
    }
}
