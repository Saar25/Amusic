package org.saartako.client.utils;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class RegionUtils {

    private RegionUtils() {
    }

    public static Region createHorizontal() {
        final Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        return region;
    }

    public static Region createVertical() {
        final Region region = new Region();
        VBox.setVgrow(region, Priority.ALWAYS);
        return region;
    }
}
