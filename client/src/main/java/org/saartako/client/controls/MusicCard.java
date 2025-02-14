package org.saartako.client.controls;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import org.saartako.client.models.CardItem;

public class MusicCard extends Control {

    private final ObjectProperty<CardItem> cardItem = new SimpleObjectProperty<>(this, "cardItem");

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
}
