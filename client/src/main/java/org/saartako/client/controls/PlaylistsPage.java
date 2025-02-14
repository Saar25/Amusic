package org.saartako.client.controls;

import javafx.scene.control.Control;

public class PlaylistsPage extends Control {

    @Override
    protected PlaylistsPageSkin createDefaultSkin() {
        return new PlaylistsPageSkin(this);
    }
}
