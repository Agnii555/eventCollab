package edu.neu.csye7374.factory;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.event.WorkshopEvent;

import java.time.LocalDate;

public class WorkshopEventFactory extends AbstractEventFactory {
    @Override
    public EventAPI createEvent(String id, String title, String description, LocalDate date, String location, int capacity) {
        return new WorkshopEvent(id, title, description, date, location, capacity);
    }
}
