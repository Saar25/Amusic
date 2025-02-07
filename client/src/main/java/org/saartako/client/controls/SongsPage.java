package org.saartako.client.controls;

import javafx.scene.control.Control;

public class SongsPage extends Control {

    @Override
    protected SongsPageSkin createDefaultSkin() {
        return new SongsPageSkin(this);
    }
}
