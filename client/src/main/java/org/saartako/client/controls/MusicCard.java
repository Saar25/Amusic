package org.saartako.client.controls;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import org.saartako.client.models.CardItem;

public class MusicCard extends Control {

    private final ObjectProperty<CardItem> cardItem = new SimpleObjectProperty<>(this, "cardItem");

    private final BooleanProperty expandable = new SimpleBooleanProperty(this, "expandable");

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
}
