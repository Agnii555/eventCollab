package edu.neu.csye7374.strategy;

import edu.neu.csye7374.event.EventAPI;

import java.util.List;

public class EventSorter {
    private SortStrategy sortStrategy;

    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public List<EventAPI> sortEvents(List<EventAPI> events) {
        if (sortStrategy == null) {
            setSortStrategy(new SortByDate());
        }
        return sortStrategy.sortEvents(events);
    }
}
