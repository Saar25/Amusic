package org.saartako.client.controls;

import javafx.scene.control.Control;

public class Amusic extends Control {

    @Override
    protected AmusicSkin createDefaultSkin() {
        return new AmusicSkin(this);
    }
}
