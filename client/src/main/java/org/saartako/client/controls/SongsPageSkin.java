package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.BorderPane;

public class SongsPageSkin implements Skin<SongsPage> {

    private final SongsPage control;

    private final Node node = new BorderPane();

    public SongsPageSkin(SongsPage control) {
        this.control = control;
    }

    @Override
    public SongsPage getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
    }
}
