package edu.neu.csye7374.manager;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.strategy.EventSorter;
import edu.neu.csye7374.strategy.SortByDate;
import edu.neu.csye7374.strategy.SortByTitle;
import edu.neu.csye7374.user.User;
import edu.neu.csye7374.user.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EventManager implements EventManagerAPI {
    
    private static EventManager instance;
    
    private Map<String, EventAPI> events;
    private Map<String, User> users;
    private Map<String, List<String>> eventRegistrations;

    private EventSorter sorter;
    
    private EventManager() {
        events = new HashMap<>();
        users = new HashMap<>();
        eventRegistrations = new HashMap<>();
        sorter = new EventSorter();
    }
    
    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }
    
    @Override
    public boolean addEvent(EventAPI event) {
        if (event != null && event.getId() != null) {
            events.put(event.getId(), event);
            eventRegistrations.put(event.getId(), new ArrayList<>());
            System.out.println("Event added: " + event.getTitle() + " (ID: " + event.getId() + ")");
            return true;
        }
        return false;
    }
    
    @Override
    public boolean registerUser(User user) {
        if (user != null && user.getId() != null) {
            users.put(user.getId(), user);
            System.out.println("User registered: " + user.getName() + " (ID: " + user.getId() + ")");
            return true;
        }
        return false;
    }
    
    @Override
    public boolean registerUserForEvent(String userId, String eventId) {
        if (!users.containsKey(userId)) {
            System.out.println("User not found: " + userId);
            return false;
        }
        
        if (!events.containsKey(eventId)) {
            System.out.println("Event not found: " + eventId);
            return false;
        }
        
        List<String> registrations = eventRegistrations.get(eventId);
        if (!registrations.contains(userId)) {
            registrations.add(userId);
            
            // If the user is a student, also update their registered events
            User user = users.get(userId);
            if (user instanceof Student) {
                ((Student) user).registerForEvent(eventId);
            }
            
            System.out.println("User " + userId + " registered for event " + eventId);
            return true;
        } else {
            System.out.println("User " + userId + " is already registered for event " + eventId);
            return false;
        }
    }
    
    /**
     * Cancel a user's registration for an event
     * @param userId ID of the user to unregister
     * @param eventId ID of the event to unregister from
     * @return true if cancellation was successful, false otherwise
     */
    public boolean cancelUserRegistration(String userId, String eventId) {
        // Check if user exists
        if (!users.containsKey(userId)) {
            System.out.println("User not found: " + userId);
            return false;
        }
        
        // Check if event exists
        if (!events.containsKey(eventId)) {
            System.out.println("Event not found: " + eventId);
            return false;
        }
        
        // Remove user from event registrations
        List<String> registrations = eventRegistrations.get(eventId);
        if (registrations != null && registrations.remove(userId)) {
            // If the user is a student, also update their registered events
            User user = users.get(userId);
            if (user instanceof Student) {
                ((Student) user).cancelEventRegistration(eventId);
            }
            
            System.out.println("User " + userId + " unregistered from event " + eventId);
            return true;
        } else {
            System.out.println("User " + userId + " was not registered for event " + eventId);
            return false;
        }
    }
    
    @Override
    public void notifyParticipants(String eventId, String message) {
        if (!events.containsKey(eventId)) {
            System.out.println("Event not found: " + eventId);
            return;
        }
        
        List<String> registrations = eventRegistrations.get(eventId);
        if (registrations != null) {
            for (String userId : registrations) {
                User user = users.get(userId);
                if (user != null) {
                    System.out.println("Notification sent to " + user.getName() + " (" + user.getEmail() + "): " + message);
                }
            }
        }
    }
    
    @Override
    public List<EventAPI> getAllEvents() {
        return sorter.sortEvents(new ArrayList<>(events.values()));
    }

    @Override
    public List<EventAPI> eventsSortedByDate() {
        sorter.setSortStrategy(new SortByDate());
        return sorter.sortEvents(new ArrayList<>(events.values()));
    }

    @Override
    public List<EventAPI> eventsSortedByTitle() {
        sorter.setSortStrategy(new SortByTitle());
        return sorter.sortEvents(new ArrayList<>(events.values()));
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public List<User> getEventParticipants(String eventId) {
        List<User> participants = new ArrayList<>();
        List<String> registrations = eventRegistrations.get(eventId);
        
        if (registrations != null) {
            for (String userId : registrations) {
                User user = users.get(userId);
                if (user != null) {
                    participants.add(user);
                }
            }
        }
        
        return participants;
    }

    @Override
    public EventAPI getEvent(String eventId) {
        return events.get(eventId);
    }
    
    
    @Override
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    
    @Override
    public boolean removeEvent(String eventId) {
        if (events.remove(eventId) != null) {
            eventRegistrations.remove(eventId);
            System.out.println("Event removed: " + eventId);
            return true;
        }
        return false;
    }
    
    
    @Override
    public String getSystemStats() {
        return String.format("EventManager Statistics - Total Events: %d, Total Users: %d", 
                           events.size(), users.size());
    }
}