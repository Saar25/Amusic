package org.saartako.client.components;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

/**
 * Loader node
 */
public class Loader extends StackPane {

    public Loader() {
        this(200);
    }

    public Loader(int size) {
        final ProgressIndicator progressIndicator = new ProgressIndicator(-1);
        progressIndicator.setMinSize(size, size);
        getChildren().addAll(progressIndicator);
    }
}
