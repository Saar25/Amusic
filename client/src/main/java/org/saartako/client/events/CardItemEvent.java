package org.saartako.client.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.saartako.client.models.CardItem;

/**
 * A card item event
 */
public class CardItemEvent extends Event {

    public static final EventType<CardItemEvent> EXPAND_CARD_ITEM = new EventType<>("EXPAND_CARD_ITEM");

    private final CardItem cardItem;

    private CardItemEvent(EventType<? extends Event> eventType, CardItem cardItem) {
        super(eventType);
        this.cardItem = cardItem;
    }

    private CardItemEvent(Object source, EventTarget target, EventType<? extends Event> eventType, CardItem cardItem) {
        super(source, target, eventType);
        this.cardItem = cardItem;
    }

    public static CardItemEvent ofExpand(CardItem cardItem) {
        return new CardItemEvent(EXPAND_CARD_ITEM, cardItem);
    }

    public CardItem getCardItem() {
        return this.cardItem;
    }
}
