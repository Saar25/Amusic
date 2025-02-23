package org.saartako.client.controls;

import atlantafx.base.controls.Card;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2AL;
import org.kordamp.ikonli.material2.Material2MZ;
import org.saartako.client.events.CardItemEvent;
import org.saartako.client.models.CardItem;
import org.saartako.client.models.MenuAction;
import org.saartako.client.utils.LayoutUtils;

import java.util.List;

public class MusicCardSkin extends SkinBase<MusicCard> {

    private final Circle headerTileGraphic = new Circle(8);

    private final Label headerTile = new Label();

    private final MenuButton menuButton = new MenuButton(null, new FontIcon(Material2MZ.MORE_VERT));

    private final VBox detailsList = new VBox();

    private final Card card = new Card();

    public MusicCardSkin(MusicCard control) {
        super(control);

        this.card.getStyleClass().addAll(Styles.INTERACTIVE);

        this.headerTile.setGraphic(this.headerTileGraphic);
        this.headerTile.getStyleClass().add(Styles.TITLE_4);

        this.menuButton.getStyleClass().addAll(Styles.FLAT, Tweaks.NO_ARROW);

        final BooleanBinding hasMenuActions = getSkinnable().menuActionsProperty().emptyProperty().not();
        this.menuButton.visibleProperty().bind(hasMenuActions);
        this.menuButton.managedProperty().bind(hasMenuActions);

        registerChangeListener(getSkinnable().menuActionsProperty(), observable ->
            updateMenuItems());
        updateMenuItems();

        final HBox header = new HBox(this.headerTile, LayoutUtils.createHorizontalSpace(), this.menuButton);
        header.setAlignment(Pos.TOP_CENTER);
        this.card.setHeader(header);

        registerChangeListener(control.cardItemProperty(), observable ->
            onCardItemChange(control.getCardItem()));

        onCardItemChange(control.getCardItem());

        this.card.setBody(LayoutUtils.createVerticalSpace());

        final Button exapndCardButton = createExpandCardButton();

        final HBox footer = new HBox(this.detailsList, LayoutUtils.createHorizontalSpace(), exapndCardButton);
        footer.setAlignment(Pos.BOTTOM_CENTER);
        this.card.setFooter(footer);

        getChildren().setAll(this.card);
    }

    private void updateMenuItems() {
        final ObservableList<MenuAction> menuActions = getSkinnable().getMenuActions();

        final List<MenuItem> menuItems = menuActions.stream().map(menuAction -> {
            final MenuItem menuItem = new MenuItem();
            menuItem.setText(menuAction.name());
            menuItem.setOnAction(menuAction.onAction());
            return menuItem;
        }).toList();

        this.menuButton.getItems().setAll(menuItems);
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
