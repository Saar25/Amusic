package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;

public class LoaderSkin extends SkinBase<Loader> {

    private final Label label = new Label("Loading...");

    public LoaderSkin(Loader control) {
        super(control);

        this.label.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLDER);

        getChildren().setAll(this.label);
    }
}
