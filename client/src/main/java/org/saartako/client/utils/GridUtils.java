package org.saartako.client.utils;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class for javafx grid pane
 */
public class GridUtils {

    private GridUtils() {
    }

    public static void initializeGrid(GridPane gridPane, int columns) {
        initializeGrid(gridPane, columns, 0, 0);
    }

    public static void initializeGrid(GridPane gridPane, int columns, int rows) {
        initializeGrid(gridPane, columns, rows, 0);
    }

    public static void initializeGrid(GridPane gridPane, int columns, int rows, int gap) {
        initializeGrid(gridPane, columns, rows, gap, 0);
    }

    public static void initializeGrid(GridPane gridPane, int columns, int rows, int gap, int padding) {
        gridPane.setVgap(gap);
        gridPane.setHgap(gap);

        final Insets insets = new Insets(padding);
        gridPane.setPadding(insets);

        final List<ColumnConstraints> cc = divideColumnConstraints(columns);
        gridPane.getColumnConstraints().addAll(cc);

        final List<RowConstraints> rc = divideRowConstraints(rows);
        gridPane.getRowConstraints().addAll(rc);
    }

    public static void addInColumns(GridPane gridPane, Iterable<? extends Node> children) {
        final int columns = gridPane.getColumnCount();
        int index = 0;
        for (Node child : children) {
            gridPane.add(child, index % columns, index / columns);
            index++;
        }
    }

    public static ColumnConstraints columnConstraintsOfPercentage(double percentage) {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(percentage);
        return cc;
    }

    public static List<ColumnConstraints> divideColumnConstraints(int count) {
        return IntStream.range(0, count).mapToObj(i -> columnConstraintsOfPercentage(100. / count)).toList();
    }

    public static RowConstraints rowConstraintsOfPercentage(double percentage) {
        final RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(percentage);
        return rc;
    }

    public static List<RowConstraints> divideRowConstraints(int count) {
        return IntStream.range(0, count).mapToObj(i -> rowConstraintsOfPercentage(100. / count)).toList();
    }
}
