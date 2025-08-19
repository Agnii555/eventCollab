package edu.neu.csye7374.decorator;

import edu.neu.csye7374.event.EventAPI;

import java.time.LocalDate;

public abstract class EventDecorator implements EventAPI {
    protected final EventAPI event;
    protected EventDecorator(EventAPI event) {
        this.event = event;
    }

    @Override public String getId() { return event.getId(); }
    @Override public String getTitle() { return event.getTitle(); }
    @Override public String getDescription() { return event.getDescription(); }
    @Override public LocalDate getDate() { return event.getDate(); }
    @Override public String getLocation() { return event.getLocation(); }
    @Override public int getCapacity() { return event.getCapacity(); }
    @Override public String getDetails() { return event.getDetails(); }
}
