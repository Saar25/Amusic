package org.saartako.client.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.util.Duration;
import org.saartako.client.enums.SongPlayerStatus;
import org.saartako.client.models.RouteNode;

public class SongView extends Control implements RouteNode {

    private final ObjectProperty<Duration> currentSongTime =
        new SimpleObjectProperty<>(this, "currentSongTime", Duration.ZERO);

    private final ObjectProperty<SongPlayerStatus> songPlayerStatus =
        new SimpleObjectProperty<>(this, "songPlayerStatus", SongPlayerStatus.STOPPED);

    public ObjectProperty<Duration> currentSongTimeProperty() {
        return this.currentSongTime;
    }

    public ObjectProperty<SongPlayerStatus> songPlayerStatusProperty() {
        return this.songPlayerStatus;
    }

    @Override
    protected SongViewSkin createDefaultSkin() {
        return new SongViewSkin(this);
    }

    @Override
    public void onExistView() {
        songPlayerStatusProperty().set(SongPlayerStatus.STOPPED);
    }

    @Override
    public void onEnterView() {
        currentSongTimeProperty().set(Duration.ZERO);
        songPlayerStatusProperty().set(SongPlayerStatus.PLAYING);
    }
}
