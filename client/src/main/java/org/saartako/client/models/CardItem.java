package org.saartako.client.models;

import javafx.scene.paint.Paint;

import java.util.Map;

public record CardItem(String name, Map<String, String> details, Paint paint) {
}
