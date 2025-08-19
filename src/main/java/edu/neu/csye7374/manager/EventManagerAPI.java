package edu.neu.csye7374.manager;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.user.User;
import java.util.List;

public interface EventManagerAPI {

    boolean addEvent(EventAPI event);

    boolean registerUser(User user);

    boolean registerUserForEvent(String userId, String eventId);

    void notifyParticipants(String eventId, String message);

    List<EventAPI> getAllEvents();

    List<EventAPI> eventsSortedByDate();

    List<EventAPI> eventsSortedByTitle();

    List<User> getAllUsers();

    List<User> getEventParticipants(String eventId);

    EventAPI getEvent(String eventId);

    User getUser(String userId);

    boolean removeEvent(String eventId);

    String getSystemStats();
}
