package org.saartako.client.controls;

import javafx.beans.binding.ListBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.saartako.client.enums.Route;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.RouterService;
import org.saartako.client.services.SongService;
import org.saartako.client.utils.BindingsUtils;
import org.saartako.common.song.Song;

import java.util.List;

public class SongsPage extends Control implements RouteNode {

    private final SongService songService = SongService.getInstance();
    private final RouterService routerService = RouterService.getInstance();

    private final ObjectProperty<String> songsFilter = new SimpleObjectProperty<>(this, "songsFilter", "");

    private final BooleanProperty onlyWithAudioFilter = new SimpleBooleanProperty(this, "onlyWithAudioFilter", false);

    private final ListBinding<Song> filteredSongs = BindingsUtils.createJavaListBinding(() -> {
        final ObservableList<Song> songs = this.songService.songsProperty();
        if (songs == null) return null;
        final String filter = this.songsFilter.get();
        if (filter == null) return songs;
        final List<Song> filteredSongs = this.songService.filterSongs(songs, filter);
        final boolean onlyWithAudioFilter = this.onlyWithAudioFilter.get();
        return onlyWithAudioFilter ? this.songService.filterSongsWithAudio(filteredSongs) : filteredSongs;
    }, this.songService.songsProperty(), this.songsFilter, this.onlyWithAudioFilter);

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

    public BooleanProperty onlyWithAudioFilterProperty() {
        return this.onlyWithAudioFilter;
    }

    public void onExpandSong(Song song) {
        this.songService.setCurrentSong(song);
        this.routerService.push(Route.SONG_VIEW);
    }
}
