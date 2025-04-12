package org.saartako.client.utils;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Utility class for javafx layout
 */
public class LayoutUtils {

    private LayoutUtils() {
    }

    public static Node createHorizontalSpace() {
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public static Node createVerticalSpace() {
        final Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }
}
