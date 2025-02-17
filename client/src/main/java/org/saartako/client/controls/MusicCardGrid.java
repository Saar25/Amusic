package org.saartako.client.controls;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import org.saartako.client.models.CardItem;

public class MusicCardGrid extends Control {

    private final ListProperty<CardItem> cardItems =
        new SimpleListProperty<>(this, "cardItems", FXCollections.observableArrayList());

    @Override
    protected MusicCardGridSkin createDefaultSkin() {
        return new MusicCardGridSkin(this);
    }

    public ListProperty<CardItem> cardItemsProperty() {
        return this.cardItems;
    }

    public ObservableList<CardItem> getCardItems() {
        return this.cardItems.get();
    }
}
