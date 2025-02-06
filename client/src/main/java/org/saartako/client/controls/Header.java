package org.saartako.client.controls;

import javafx.scene.control.Control;

public class Header extends Control {

    @Override
    protected HeaderSkin createDefaultSkin() {
        return new HeaderSkin(this);
    }
}
