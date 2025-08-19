package edu.neu.csye7374.factory;

import edu.neu.csye7374.event.EventAPI;

import java.time.LocalDate;

public abstract class AbstractEventFactory {
    public abstract EventAPI createEvent(String id, String title, String description, LocalDate date, String location, int capacity);
}
