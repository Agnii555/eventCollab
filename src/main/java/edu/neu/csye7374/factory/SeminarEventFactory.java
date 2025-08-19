package edu.neu.csye7374.factory;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.event.SeminarEvent;

import java.time.LocalDate;

public class SeminarEventFactory extends AbstractEventFactory {
    @Override
    public EventAPI createEvent(String id, String title, String description, LocalDate date, String location, int capacity) {
        return new SeminarEvent(id, title, description, date, location, capacity);
    }
}
