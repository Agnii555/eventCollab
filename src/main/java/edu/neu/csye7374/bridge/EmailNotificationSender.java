package edu.neu.csye7374.bridge;

import edu.neu.csye7374.user.User;
import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.manager.EventManager;
import java.util.List;

public class EmailNotificationSender implements NotificationSender {

    @Override
    public boolean sendToUser(User user, String subject, String body) {
        System.out.println("📧 [EMAIL] EmailNotificationSender.sendToUser() called");
        System.out.println("📧 [EMAIL] Recipient: " + user.getName() + " (" + user.getEmail() + ")");
        System.out.println("📧 [EMAIL] Subject: " + subject);
        System.out.println("📧 [EMAIL] Body: " + body);
        System.out.println("📧 [EMAIL] User ID: " + user.getId());
        System.out.println("📧 [EMAIL] User Type: " + user.getClass().getSimpleName());
        
        // Simulate email sending
        System.out.println("📧 [EMAIL] Simulating email delivery...");
        System.out.println("📧 [EMAIL] [EMAIL] To: " + user.getEmail() + " | " + subject + " -- " + body);
        
        System.out.println("📧 [EMAIL] Email sent successfully!");
        return true;
    }

    @Override
    public boolean sendToUsers(List<User> users, String subject, String body) {
        users.forEach(u -> sendToUser(u, subject, body));
        return true;
    }

    @Override
    public boolean sendToEventParticipants(EventAPI event, String subject, String body) {
        List<User> participants = EventManager.getInstance().getEventParticipants(event.getId());
        return sendToUsers(participants, subject, body);
    }
}
