package org.saartako.client.controls;

import atlantafx.base.theme.Styles;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.saartako.client.models.CardItem;

import java.util.List;

public class MusicCardGridSkin implements Skin<MusicCardGrid> {

    private static final int COLUMN_COUNT = 3;

    private final MusicCardGrid control;

    private final ScrollPane node = new ScrollPane();

    private final GridPane gridPane = new GridPane();

    public MusicCardGridSkin(MusicCardGrid control) {
        this.control = control;

        this.node.setFitToWidth(true);

        final VBox vBox = new VBox(20);
        vBox.setAlignment(Pos.TOP_CENTER);
        this.node.setContent(vBox);

        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setVgap(16);
        this.gridPane.setHgap(16);

        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / COLUMN_COUNT);
        cc.setHalignment(HPos.CENTER);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            this.gridPane.getColumnConstraints().add(cc);
        }

        vBox.getChildren().add(this.gridPane);

        this.control.cardItemsProperty().addListener((observable, oldValue, newValue) ->
            onCardItemsChange(newValue));

        onCardItemsChange(this.control.getCardItems());
    }

    private void onCardItemsChange(List<? extends CardItem> cardItems) {
        if (cardItems == null) {
            showLoading();
        } else {
            showCardItems(cardItems);
        }
    }

    private void showLoading() {
        final Label label = new Label("Loading...");
        label.getStyleClass().addAll("title-big-1", Styles.TEXT_BOLDER);
        GridPane.setColumnSpan(label, 3);
        this.gridPane.getChildren().setAll(label);
    }

    private void showCardItems(List<? extends CardItem> cardItems) {
        this.gridPane.getChildren().clear();
        for (int i = 0; i < cardItems.size(); i++) {
            final MusicCard musicCard = new MusicCard();
            musicCard.setCardItem(cardItems.get(i));

            this.gridPane.add(musicCard, i % 3, i / 3);
        }
    }

    @Override
    public MusicCardGrid getSkinnable() {
        return this.control;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public void dispose() {
    }
}
