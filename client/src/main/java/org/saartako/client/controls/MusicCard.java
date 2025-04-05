package org.saartako.client.controls;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.saartako.client.models.CardItem;
import org.saartako.client.models.MenuAction;

/**
 * Music card control, showing all the details given in the card item
 */
public class MusicCard extends Control {

    private final ObjectProperty<CardItem> cardItem = new SimpleObjectProperty<>(this, "cardItem");

    private final BooleanProperty expandable = new SimpleBooleanProperty(this, "expandable");

    private final ListProperty<MenuAction> menuActions = new SimpleListProperty<>(
        this, "menuActions", FXCollections.observableArrayList());

    public MusicCard() {
    }

    public MusicCard(CardItem cardItem) {
        this.cardItem.set(cardItem);
    }

    @Override
    protected MusicCardSkin createDefaultSkin() {
        return new MusicCardSkin(this);
    }

    public ObjectProperty<CardItem> cardItemProperty() {
        return this.cardItem;
    }

    public CardItem getCardItem() {
        return this.cardItem.get();
    }

    public void setCardItem(CardItem cardItem) {
        this.cardItem.set(cardItem);
    }

    public BooleanProperty expandableProperty() {
        return this.expandable;
    }

    public boolean isExpandable() {
        return this.expandable.get();
    }

    public void setExpandable(boolean expandable) {
        this.expandable.set(expandable);
    }

    public ListProperty<MenuAction> menuActionsProperty() {
        return this.menuActions;
    }

    public ObservableList<MenuAction> getMenuActions() {
        return this.menuActions.get();
    }

    public void setMenuActions(ObservableList<MenuAction> menuActions) {
        this.menuActions.set(menuActions);
    }
}
