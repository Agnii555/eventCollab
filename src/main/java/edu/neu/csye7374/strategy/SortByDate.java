package edu.neu.csye7374.strategy;

import edu.neu.csye7374.event.EventAPI;

import java.util.Comparator;
import java.util.List;

public class SortByDate implements SortStrategy {
    @Override
    public List<EventAPI> sortEvents(List<EventAPI> events) {
        System.out.println("Sorting events by date...");

        events.sort(Comparator.comparing(EventAPI::getDate).reversed());

        return events;
    }
}
