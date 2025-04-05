package org.saartako.client.controls;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import org.saartako.client.enums.Route;

import java.util.Map;

/**
 * Skin for {@link Amusic}
 */
public class AmusicSkin extends SkinBase<Amusic> {

    public AmusicSkin(Amusic control) {
        super(control);
        final Node defaultRoute = new Label("Page not found");
        defaultRoute.getStyleClass().addAll("title-big-1", "danger");

        final Map<Route, Node> routes = getSkinnable().getRoutes();
        final Router center = new Router(routes, defaultRoute);
        final Header top = new Header();

        final BorderPane node = new BorderPane();
        node.setCenter(center);
        node.setTop(top);

        getChildren().setAll(node);
    }
}
