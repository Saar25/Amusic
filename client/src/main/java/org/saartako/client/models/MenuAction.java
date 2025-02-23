package org.saartako.client.models;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public record MenuAction(String name, EventHandler<ActionEvent> onAction) {
}
