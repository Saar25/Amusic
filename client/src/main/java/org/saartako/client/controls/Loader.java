package org.saartako.client.controls;

import javafx.scene.control.Control;

public class Loader extends Control {

    @Override
    protected LoaderSkin createDefaultSkin() {
        return new LoaderSkin(this);
    }
}
