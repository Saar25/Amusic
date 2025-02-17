package org.saartako.client.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import org.saartako.client.models.CardItem;

public class CardItemClickEvent extends Event {

    public static final EventType<CardItemClickEvent> CARD_ITEM_CLICK = new EventType<>("CARD_ITEM_CLICK");

    private final CardItem cardItem;

    private CardItemClickEvent(EventType<? extends Event> eventType, CardItem cardItem) {
        super(eventType);
        this.cardItem = cardItem;
    }

    private CardItemClickEvent(Object source, EventTarget target, EventType<? extends Event> eventType, CardItem cardItem) {
        super(source, target, eventType);
        this.cardItem = cardItem;
    }

    public static CardItemClickEvent ofClick(CardItem cardItem) {
        return new CardItemClickEvent(CARD_ITEM_CLICK, cardItem);
    }

    public CardItem getCardItem() {
        return this.cardItem;
    }
}
