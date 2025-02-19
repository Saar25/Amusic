package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;

public class LoaderSkin implements Skin<Loader> {

    private final Loader control;

    private final Label node = new Label("Loading...");

    public LoaderSkin(Loader control) {
        this.control = control;

        this.node.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLDER);
    }

    @Override
    public Loader getSkinnable() {
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
