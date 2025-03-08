package org.saartako.client.controls;

import javafx.scene.control.Control;
import org.saartako.client.models.RouteNode;
import org.saartako.client.services.PlaylistService;

public class PlaylistsPage extends Control implements RouteNode {

    private final PlaylistService playlistService = PlaylistService.getInstance();

    @Override
    protected PlaylistsPageSkin createDefaultSkin() {
        return new PlaylistsPageSkin(this);
    }

    @Override
    public void onEnterView() {
        this.playlistService.fetchData();
    }
}
