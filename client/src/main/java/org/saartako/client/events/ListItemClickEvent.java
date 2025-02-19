package org.saartako.client.events;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class ListItemClickEvent extends Event {

    public static final EventType<ListItemClickEvent> LIST_ITEM_CLICK = new EventType<>("LIST_ITEM_CLICK");

    private final int index;

    private ListItemClickEvent(EventType<? extends Event> eventType, int index) {
        super(eventType);
        this.index = index;
    }

    private ListItemClickEvent(Object source, EventTarget target, EventType<? extends Event> eventType, int index) {
        super(source, target, eventType);
        this.index = index;
    }

    public static ListItemClickEvent ofClick(int index) {
        return new ListItemClickEvent(LIST_ITEM_CLICK, index);
    }

    public int getIndex() {
        return this.index;
    }
}
