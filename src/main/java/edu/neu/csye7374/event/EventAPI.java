package edu.neu.csye7374.event;

import java.time.LocalDate;

public interface EventAPI {
    String getId();
    String getTitle();
    String getDescription();
    LocalDate getDate();
    String getLocation();
    int getCapacity();

    String getDetails();
}
