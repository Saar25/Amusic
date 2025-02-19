package org.saartako.client.controls;

import javafx.scene.control.Control;

public class PlaylistView extends Control {

    @Override
    protected PlaylistViewSkin createDefaultSkin() {
        return new PlaylistViewSkin(this);
    }
}
