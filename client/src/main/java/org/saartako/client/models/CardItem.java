package org.saartako.client.models;

import javafx.scene.paint.Paint;

import java.util.Map;

public record CardItem(long id, String name, Map<String, String> details, Paint paint) {
}
