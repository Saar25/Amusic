package org.saartako.client.components;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;

public class Loader extends StackPane {

    public Loader() {
        final ProgressIndicator progressIndicator = new ProgressIndicator(-1);
        progressIndicator.setMinSize(200, 200);
        getChildren().addAll(progressIndicator);
    }
}
