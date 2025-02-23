package org.saartako.client.controls;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.events.CardItemEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.utils.LayoutUtils;

public class MusicCardSkin extends SkinBase<MusicCard> {

    private final Circle headerTileGraphic = new Circle(8);

    private final Label headerTile = new Label();

    private final VBox detailsList = new VBox();

    private final Card card = new Card();

    public MusicCardSkin(MusicCard control) {
        super(control);

        this.card.getStyleClass().addAll(Styles.INTERACTIVE);

        this.headerTile.setGraphic(this.headerTileGraphic);
        this.headerTile.getStyleClass().add(Styles.TITLE_4);

        final MenuButton menuButton = new MenuButton(null, new FontIcon(Material2MZ.MORE_VERT));
        menuButton.getStyleClass().addAll(Styles.FLAT, Tweaks.NO_ARROW);
        this.card.setHeader(new HBox(this.headerTile, LayoutUtils.createHorizontalSpace(), menuButton));

        registerChangeListener(control.cardItemProperty(), (observable) ->
            onCardItemChange(control.getCardItem()));

        onCardItemChange(control.getCardItem());

        this.card.setBody(LayoutUtils.createVerticalSpace());

        final Button button = createExpandCardButton();

        final HBox footer = new HBox(this.detailsList, LayoutUtils.createHorizontalSpace(), button);
        footer.setAlignment(Pos.BOTTOM_CENTER);
        this.card.setFooter(footer);

        getChildren().setAll(this.card);
    }

    private void onCardItemChange(CardItem cardItem) {
        if (cardItem == null) return;

        this.detailsList.getChildren().clear();

        cardItem.details().forEach((key, value) -> {
            final Label label = new Label(key + ": " + value);
            this.detailsList.getChildren().add(label);
        });

        final Paint cardItemColor = cardItem.paint();
        this.headerTileGraphic.setFill(cardItemColor);
        this.headerTile.setText(cardItem.name());
    }

    private Button createExpandCardButton() {
        final Button button = new Button(null, new FontIcon(Material2AL.ARROW_FORWARD));
        button.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);
        button.setOnAction(e -> {
            final CardItem cardItem = getSkinnable().getCardItem();
            final CardItemEvent event = CardItemEvent.ofExpand(cardItem);
            getSkinnable().fireEvent(event);
        });

        button.visibleProperty().bind(getSkinnable().expandableProperty());
        button.managedProperty().bind(getSkinnable().expandableProperty());
        return button;
    }
}
