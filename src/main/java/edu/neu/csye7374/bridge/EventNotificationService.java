package edu.neu.csye7374.bridge;

import edu.neu.csye7374.user.User;
import edu.neu.csye7374.event.EventAPI;
import java.util.List;

public class EventNotificationService extends NotificationServiceBridge {

    public EventNotificationService(NotificationSender sender) { super(sender); }

    @Override
    public boolean notifyUser(User user, String subject, String body) {
        System.out.println("🌉 [BRIDGE] EventNotificationService.notifyUser() called");
        System.out.println("🌉 [BRIDGE] User: " + user.getName() + " (" + user.getEmail() + ")");
        System.out.println("🌉 [BRIDGE] Subject: " + subject);
        System.out.println("🌉 [BRIDGE] Body: " + body);
        System.out.println("🌉 [BRIDGE] Notification sender type: " + sender.getClass().getSimpleName());
        
        System.out.println("🌉 [BRIDGE] Calling sender.sendToUser()...");
        boolean result = sender.sendToUser(user, subject, body);
        
        System.out.println("🌉 [BRIDGE] sender.sendToUser() returned: " + result);
        return result;
    }

    @Override
    public boolean notifyEventParticipants(EventAPI event, String subject, String body) {
        return sender.sendToEventParticipants(event, subject, body);
    }

    @Override
    public boolean notifyAllUsers(List<User> users, String subject, String body) {
        return sender.sendToUsers(users, subject, body);
    }

    // domain-friendly helpers
    public boolean sendRegistrationConfirmation(User user, EventAPI event) {
        return notifyUser(user,
            "Registration Confirmed: " + event.getTitle(),
            "You're registered for " + event.getTitle() + " on " + event.getDate()
        );
    }

    public boolean sendEventCreatedNotification(EventAPI event) {
        return notifyEventParticipants(event,
            "New Event: " + event.getTitle(),
            "An event has been created: " + event.getDescription()
        );
    }
}
