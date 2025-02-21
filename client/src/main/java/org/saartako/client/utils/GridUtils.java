package org.saartako.client.utils;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.util.List;
import java.util.stream.IntStream;

public class GridUtils {

    private GridUtils() {
    }

    public static ColumnConstraints columnConstraintsOfPercentage(double percentage) {
        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(percentage);
        return cc;
    }

    public static List<ColumnConstraints> divideColumnConstraints(int count) {
        final ColumnConstraints columnConstraints = columnConstraintsOfPercentage(100. / count);

        return IntStream.range(0, count).mapToObj(i -> columnConstraintsOfPercentage(100. / count)).toList();
    }

    public static RowConstraints rowConstraintsOfPercentage(double percentage) {
        final RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(percentage);
        return rc;
    }

    public static List<RowConstraints> divideRowConstraints(int count) {
        final RowConstraints rowConstraints = rowConstraintsOfPercentage(100. / count);

        return IntStream.range(0, count).mapToObj(i -> rowConstraintsOfPercentage(100. / count)).toList();
    }
}
