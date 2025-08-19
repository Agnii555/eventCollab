package edu.neu.csye7374.factory;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.event.LectureEvent;

import java.time.LocalDate;

public class LectureEventFactory extends AbstractEventFactory {
    @Override
    public EventAPI createEvent(String id, String title, String description, LocalDate date, String location, int capacity) {
        return new LectureEvent(id, title, description, date, location, capacity);
    }
}
