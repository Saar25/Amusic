package org.saartako.client.controls;

import atlantafx.base.util.Animations;
import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.saartako.client.models.RouteNode;

/**
 * Skin for {@link Router}
 */
public class RouterSkin extends SkinBase<Router> {

    private final StackPane node = new StackPane();

    public RouterSkin(Router control) {
        super(control);

        registerChangeListener(getSkinnable().currentRouteNodeProperty(), observable -> onRouteChange());
        onRouteChange();

        getChildren().setAll(this.node);
    }

    private void onRouteChange() {
        final Node routeNode = getSkinnable().currentRouteNodeProperty().get();
        final Node oldChild = this.node.getChildren().isEmpty() ? null : this.node.getChildren().get(0);

        if (routeNode == null) {
            this.node.getChildren().clear();
        } else {
            Animations.fadeIn(routeNode, Duration.seconds(1)).playFromStart();

            this.node.getChildren().setAll(routeNode);
            if (routeNode instanceof RouteNode routerNode) {
                routerNode.onEnterView();
            }
        }
        if (oldChild instanceof RouteNode routerNode) {
            routerNode.onExistView();
        }
    }
}