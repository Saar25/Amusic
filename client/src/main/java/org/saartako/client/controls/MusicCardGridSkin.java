package org.saartako.client.controls;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.saartako.client.events.CardItemClickEvent;
import org.saartako.client.models.CardItem;

import java.util.List;

public class MusicCardGridSkin implements Skin<MusicCardGrid> {

    private static final int COLUMN_COUNT = 3;

    private final MusicCardGrid control;

    private final GridPane node = new GridPane();

    public MusicCardGridSkin(MusicCardGrid control) {
        this.control = control;

        this.node.setAlignment(Pos.CENTER);
        this.node.setVgap(16);
        this.node.setHgap(16);

        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / COLUMN_COUNT);
        cc.setHalignment(HPos.CENTER);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            this.node.getColumnConstraints().add(cc);
        }

        this.control.cardItemsProperty().addListener((observable, oldValue, newValue) ->
            onCardItemsChange(newValue));

        onCardItemsChange(this.control.getCardItems());
    }

    private void onCardItemsChange(List<? extends CardItem> cardItems) {
        this.node.getChildren().clear();
        for (int i = 0; i < cardItems.size(); i++) {
            final CardItem cardItem = cardItems.get(i);

            final MusicCard musicCard = new MusicCard();
            musicCard.setCardItem(cardItem);
            musicCard.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                final CardItemClickEvent event =
                    CardItemClickEvent.ofClick(cardItem);
                this.control.fireEvent(event);
            });

            this.node.add(musicCard, i % COLUMN_COUNT, i / COLUMN_COUNT);
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
