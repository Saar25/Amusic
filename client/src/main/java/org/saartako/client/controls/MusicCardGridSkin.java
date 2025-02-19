package org.saartako.client.controls;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.saartako.client.events.ListItemClickEvent;
import org.saartako.client.models.CardItem;

import java.util.List;

public class MusicCardGridSkin extends SkinBase<MusicCardGrid> {

    private static final int COLUMN_COUNT = 3;

    private final GridPane gridPane = new GridPane();

    public MusicCardGridSkin(MusicCardGrid control) {
        super(control);

        this.gridPane.setAlignment(Pos.CENTER);
        this.gridPane.setVgap(16);
        this.gridPane.setHgap(16);

        final ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(100d / COLUMN_COUNT);
        cc.setHalignment(HPos.CENTER);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            this.gridPane.getColumnConstraints().add(cc);
        }

        registerListChangeListener(control.cardItemsProperty(), observable ->
            onCardItemsChange(control.getCardItems()));
        onCardItemsChange(control.getCardItems());

        getChildren().setAll(this.gridPane);
    }

    private void onCardItemsChange(List<? extends CardItem> cardItems) {
        this.gridPane.getChildren().clear();
        for (int i = 0; i < cardItems.size(); i++) {
            final CardItem cardItem = cardItems.get(i);
            final int index = i;

            final MusicCard musicCard = new MusicCard();
            musicCard.setCardItem(cardItem);
            musicCard.addEventFilter(MouseEvent.MOUSE_CLICKED, e ->
                getNode().fireEvent(ListItemClickEvent.ofClick(index)));

            this.gridPane.add(musicCard, i % COLUMN_COUNT, i / COLUMN_COUNT);
        }
    }
}
