package org.saartako.client.controls;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.saartako.client.models.CardItem;

import java.util.Map;

public class MusicCardSkin extends SkinBase<MusicCard> {

    private final Circle headerTileGraphic = new Circle(8);

    private final Label headerTile = new Label();

    private final VBox footer = new VBox();

    private final Card card = new Card();

    public MusicCardSkin(MusicCard control) {
        super(control);

        this.card.getStyleClass().addAll(Styles.INTERACTIVE);

        this.headerTile.setGraphic(this.headerTileGraphic);
        this.headerTile.getStyleClass().add(Styles.TITLE_4);
        this.card.setHeader(this.headerTile);

        registerChangeListener(control.cardItemProperty(), (observable) ->
            onCardItemChange(control.getCardItem()));

        onCardItemChange(control.getCardItem());

        this.card.setFooter(this.footer);

        getChildren().setAll(this.card);
    }

    private void onCardItemChange(CardItem cardItem) {
        if (cardItem == null) return;

        this.footer.getChildren().clear();

        cardItem.details().forEach((key, value) -> {
            final Label label = new Label(key + ": " + value);
            this.footer.getChildren().add(label);
        });

        final Paint cardItemColor = cardItem.paint();
        this.headerTileGraphic.setFill(cardItemColor);
        this.headerTile.setText(cardItem.name());
    }
}
