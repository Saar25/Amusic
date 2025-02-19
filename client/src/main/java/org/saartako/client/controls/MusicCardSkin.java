package org.saartako.client.controls;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.saartako.client.models.CardItem;

import java.util.Map;

public class MusicCardSkin implements Skin<MusicCard> {

    private final MusicCard control;

    private final Circle headerTileGraphic = new Circle(8);

    private final Label headerTile = new Label();

    private final VBox footer = new VBox();

    private final Card node = new Card();

    public MusicCardSkin(MusicCard control) {
        this.control = control;

        this.node.getStyleClass().addAll(Styles.INTERACTIVE);

        this.headerTile.setGraphic(this.headerTileGraphic);
        this.headerTile.getStyleClass().add(Styles.TITLE_4);
        this.node.setHeader(this.headerTile);

        this.control.cardItemProperty().addListener((observable, oldValue, newValue) ->
            onCardItemChange(newValue));
        onCardItemChange(this.control.getCardItem());

        this.node.setFooter(this.footer);
    }

    private void onCardItemChange(CardItem cardItem) {
        if (cardItem == null) return;

        this.footer.getChildren().clear();

        for (Map.Entry<String, String> detail : cardItem.details().entrySet()) {
            final String key = detail.getKey();
            final String value = detail.getValue();
            final Label label = new Label(key + ": " + value);
            this.footer.getChildren().add(label);
        }

        final Paint cardItemColor = cardItem.paint();
        this.headerTileGraphic.setFill(cardItemColor);
        this.headerTile.setText(cardItem.name());
    }

    @Override
    public MusicCard getSkinnable() {
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
