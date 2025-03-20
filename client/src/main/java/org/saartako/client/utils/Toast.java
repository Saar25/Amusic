package org.saartako.client.utils;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.saartako.client.Config;


public class Toast {

    private static final Duration ALIVE_DURATION = Duration.seconds(5);
    private static final Duration LEAVE_DURATION = Duration.millis(500);

    private final Notification notification;
    private final Toast.Provider provider;

    private Toast(Notification notification, Provider provider) {
        this.notification = notification;
        this.provider = provider;
    }

    public void show() {
        final StackPane parent = new StackPane(this.notification);
        StackPane.setAlignment(this.notification, Pos.BOTTOM_LEFT);
        StackPane.setMargin(this.notification, new Insets(Config.GAP_MEDIUM));

        Platform.runLater(() -> {
            final Timeline enterAnimation = Animations.shakeY(this.notification);
            this.provider.addChild(parent);
            enterAnimation.playFromStart();
        });

        this.notification.getStyleClass().addAll(Styles.DANGER, Styles.ELEVATED_1);
        this.notification.setPrefHeight(Region.USE_PREF_SIZE);
        this.notification.setMaxHeight(Region.USE_PREF_SIZE);

        this.notification.setOnClose(e -> {
            final Timeline timeline = Animations.slideOutDown(this.notification, LEAVE_DURATION);
            timeline.setOnFinished(f -> this.provider.removeChild(parent));
            timeline.playFromStart();
        });

        new Thread(() -> {
            try {
                Thread.sleep((int) ALIVE_DURATION.toMillis());
            } catch (InterruptedException ignored) {
            }
            Platform.runLater(() -> this.notification.getOnClose().handle(null));
        }).start();
    }

    public static abstract class Provider {
        public Toast provide(String message, Node graphic) {
            final Notification notification = new Notification(message, graphic);

            return new Toast(notification, this);
        }

        protected abstract void addChild(Node child);

        protected abstract void removeChild(Node child);
    }
}
