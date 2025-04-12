package org.saartako.client.models;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Record class of menu action
 *
 * @param name     the name of the action
 * @param onAction the action
 */
public record MenuAction(String name, EventHandler<ActionEvent> onAction) {
}
