package org.saartako.client.models;

import javafx.scene.paint.Paint;

import java.util.Map;

/**
 * Record class of a card item
 *
 * @param name    the name of the card
 * @param details a map of the card details
 * @param paint   the card fill
 */
public record CardItem(String name, Map<String, String> details, Paint paint) {
}
