package edu.neu.csye7374.command;

import edu.neu.csye7374.manager.EventManager;

/**
 * Concrete command for registering a user for an event
 * Follows professor's style with direct parameters and receiver reference
 */
public class RegisterEventCommand implements Command {
    
    private String userId;
    private String eventId;
    private EventManager eventManager;
    
    /**
     * Constructor for RegisterEventCommand
     * @param userId ID of the user to register
     * @param eventId ID of the event to register for
     */
    public RegisterEventCommand(String userId, String eventId) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventManager = EventManager.getInstance(); // Get singleton instance
    }
    
    /**
     * Execute the registration command
     * @return true if registration was successful, false otherwise
     */
    @Override
    public boolean execute() {
        System.out.println("Command: Registering user " + userId + " for event " + eventId);
        return eventManager.registerUserForEvent(userId, eventId);
    }
    
    // Getters for testing/debugging purposes
    public String getUserId() {
        return userId;
    }
    
    public String getEventId() {
        return eventId;
    }
}