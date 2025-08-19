package edu.neu.csye7374.strategy;

import edu.neu.csye7374.event.EventAPI;

import java.util.List;

public interface SortStrategy {
    List<EventAPI> sortEvents(List<EventAPI> events);
}
