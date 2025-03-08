package org.saartako.client.controls;

import javafx.scene.control.Control;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.SongService;

public class SongsPage extends Control implements RouteNode {

    private final SongService songService = SongService.getInstance();

    @Override
    protected SongsPageSkin createDefaultSkin() {
        return new SongsPageSkin(this);
    }

    @Override
    public void onEnterView() {
        this.songService.fetchData();
    }
}
