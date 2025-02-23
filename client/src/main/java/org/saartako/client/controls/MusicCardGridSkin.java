package org.saartako.client.controls;

import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.saartako.client.Config;
import org.saartako.client.events.ListItemClickEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.utils.GridUtils;

import java.util.List;

public class MusicCardGridSkin extends SkinBase<MusicCardGrid> {

    private final GridPane gridPane = new GridPane();

    public MusicCardGridSkin(MusicCardGrid control) {
        super(control);

        GridUtils.initializeGrid(this.gridPane, Config.GRID_LARGE_COLUMNS, 0, Config.GAP_LARGE, Config.GAP_MEDIUM);

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

            this.gridPane.add(musicCard, i % Config.GRID_LARGE_COLUMNS, i / Config.GRID_LARGE_COLUMNS);
        }
    }
}
