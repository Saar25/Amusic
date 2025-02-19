package org.saartako.client.controls;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.saartako.client.events.CardItemClickEvent;
import org.saartako.client.models.CardItem;

import java.util.List;

public class MusicCardGridSkin implements Skin<MusicCardGrid> {

    private static final int COLUMN_COUNT = 3;

    private final MusicCardGrid control;

    private final ScrollPane node = new ScrollPane();

    private final GridPane gridPane = new GridPane();
    private final Loader loader = new Loader();

    public MusicCardGridSkin(MusicCardGrid control) {
        this.control = control;

        this.node.setFitToWidth(true);

        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setVgap(16);
        this.gridPane.setHgap(16);

        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / COLUMN_COUNT);
        cc.setHalignment(HPos.CENTER);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            this.gridPane.getColumnConstraints().add(cc);
        }

        this.control.cardItemsProperty().addListener((observable, oldValue, newValue) ->
            onCardItemsChange(newValue));

        onCardItemsChange(this.control.getCardItems());
    }

    private void onCardItemsChange(List<? extends CardItem> cardItems) {
        if (cardItems == null) {
            this.node.setContent(this.loader);
        } else {
            showCardItems(cardItems);
            this.node.setContent(this.gridPane);
        }
    }

    private void showCardItems(List<? extends CardItem> cardItems) {
        this.gridPane.getChildren().clear();
        for (int i = 0; i < cardItems.size(); i++) {
            final CardItem cardItem = cardItems.get(i);

            final MusicCard musicCard = new MusicCard();
            musicCard.setCardItem(cardItem);
            musicCard.setOnMouseClicked(e -> {
                final CardItemClickEvent event =
                    CardItemClickEvent.ofClick(cardItem);
                this.control.fireEvent(event);
            });

            this.gridPane.add(musicCard, i % COLUMN_COUNT, i / COLUMN_COUNT);
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
