package org.saartako.client.controls;

import javafx.scene.control.Control;

public class SongView extends Control {

    @Override
    protected SongViewSkin createDefaultSkin() {
        return new SongViewSkin(this);
    }
}
