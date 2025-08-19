package edu.neu.csye7374.service;

import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.user.Organizer;
import edu.neu.csye7374.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling all notification operations in the system.
 * Used by the facade to provide notification functionality.
 */
public class NotificationService {
    
    private Map<String, List<String>> notificationLog;
    
    public NotificationService() {
        this.notificationLog = new HashMap<>();
    }
    
    /**
     * Send welcome notification to new users
     */
    public void sendWelcomeNotification(User user) {
        String message = "Welcome to Campus Event System, " + user.getName() + "!";
        sendNotification(user, message);
        System.out.println("Welcome notification sent to " + user.getName() + " (" + user.getEmail() + ")");
    }
    
    /**
     * Send login notification
     */
    public void sendLoginNotification(User user) {
        String message = "Successfully logged in at " + LocalDateTime.now();
        sendNotification(user, message);
        System.out.println("Login notification sent to " + user.getName());
    }
    
    /**
     * Send event creation notification to organizer
     */
    public void sendEventCreationNotification(Organizer organizer, EventAPI event) {
        String message = "Event '" + event.getTitle() + "' has been successfully created and is now live!";
        sendNotification(organizer, message);
        System.out.println("Event creation notification sent to organizer " + organizer.getName());
    }
    
    /**
     * Send registration confirmation to user
     */
    public void sendRegistrationConfirmation(User user, EventAPI event) {
        String message = "You have been successfully registered for '" + event.getTitle() + "' on " + event.getDate();
        sendNotification(user, message);
        System.out.println("Registration confirmation sent to " + user.getName());
    }
    
    /**
     * Send cancellation confirmation to user
     */
    public void sendCancellationConfirmation(User user, EventAPI event) {
        String message = "Your registration for '" + event.getTitle() + "' has been cancelled successfully.";
        sendNotification(user, message);
        System.out.println("Cancellation confirmation sent to " + user.getName());
    }
    
    /**
     * Send system-wide announcement to all users
     */
    public void sendSystemAnnouncement(List<User> users, String message) {
        for (User user : users) {
            String personalizedMessage = "System Announcement: " + message;
            sendNotification(user, personalizedMessage);
        }
        System.out.println("System announcement sent to " + users.size() + " users");
    }
    
    /**
     * Send event reminder notification
     */
    public void sendEventReminder(User user, EventAPI event, int hoursBeforeEvent) {
        String message = String.format("Reminder: Event '%s' is starting in %d hours at %s", 
                                     event.getTitle(), hoursBeforeEvent, event.getLocation());
        sendNotification(user, message);
        System.out.println("Event reminder sent to " + user.getName());
    }
    
    /**
     * Send event update notification
     */
    public void sendEventUpdateNotification(User user, EventAPI event, String updateDetails) {
        String message = String.format("Event Update: '%s' has been updated. Details: %s", 
                                     event.getTitle(), updateDetails);
        sendNotification(user, message);
        System.out.println("Event update notification sent to " + user.getName());
    }
    
    /**
     * Send event cancellation notification
     */
    public void sendEventCancellationNotification(User user, EventAPI event) {
        String message = String.format("Event Cancelled: '%s' scheduled for %s has been cancelled.", 
                                     event.getTitle(), event.getDate());
        sendNotification(user, message);
        System.out.println("Event cancellation notification sent to " + user.getName());
    }
    
    /**
     * Generic notification sender
     */
    private void sendNotification(User user, String message) {
        // Log the notification
        String userId = user.getId();
        notificationLog.computeIfAbsent(userId, k -> new ArrayList<>()).add(
            LocalDateTime.now() + ": " + message
        );
        
        // In a real system, this would send email, SMS, push notification, etc.
        // For now, we'll just simulate the notification
        System.out.println("📧 Notification to " + user.getName() + " (" + user.getEmail() + "): " + message);
    }
    
    /**
     * Log notification for event
     */
    public void logNotification(String eventId, String message) {
        String logKey = "event_" + eventId;
        notificationLog.computeIfAbsent(logKey, k -> new ArrayList<>()).add(
            LocalDateTime.now() + ": " + message
        );
        System.out.println("Notification logged for event " + eventId);
    }
    
    /**
     * Get notification history for a user
     */
    public List<String> getNotificationHistory(String userId) {
        return notificationLog.getOrDefault(userId, new ArrayList<>());
    }
    
    /**
     * Get notification history for an event
     */
    public List<String> getEventNotificationHistory(String eventId) {
        String logKey = "event_" + eventId;
        return notificationLog.getOrDefault(logKey, new ArrayList<>());
    }
    
    /**
     * Clear notification history for a user
     */
    public void clearNotificationHistory(String userId) {
        notificationLog.remove(userId);
        System.out.println("Notification history cleared for user " + userId);
    }
    
    /**
     * Get total number of notifications sent
     */
    public int getTotalNotificationsSent() {
        return notificationLog.values().stream()
                              .mapToInt(List::size)
                              .sum();
    }
}