package edu.neu.csye7374.command;

import edu.neu.csye7374.manager.EventManager;

/**
 * Concrete command for cancelling a user's event registration
 * Follows professor's style with direct parameters and receiver reference
 */
public class CancelEventCommand implements Command {
    
    private String userId;
    private String eventId;
    private EventManager eventManager;
    
    /**
     * Constructor for CancelEventCommand
     * @param userId ID of the user whose registration to cancel
     * @param eventId ID of the event to cancel registration for
     */
    public CancelEventCommand(String userId, String eventId) {
        this.userId = userId;
        this.eventId = eventId;
        this.eventManager = EventManager.getInstance(); // Get singleton instance
    }
    
    /**
     * Execute the cancellation command
     * @return true if cancellation was successful, false otherwise
     */
    @Override
    public boolean execute() {
        System.out.println("Command: Cancelling registration for user " + userId + " from event " + eventId);
        // Simply delegate to EventManager's new cancelUserRegistration method
        return eventManager.cancelUserRegistration(userId, eventId);
    }
    
    // Getters for testing/debugging purposes
    public String getUserId() {
        return userId;
    }
    
    public String getEventId() {
        return eventId;
    }
}