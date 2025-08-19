package edu.neu.csye7374.bridge;

import edu.neu.csye7374.user.User;
import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.manager.EventManager;
import java.util.List;

public class SmsNotificationSender implements NotificationSender {
    @Override
    public boolean sendToUser(User user, String subject, String body) {
        System.out.println("[SMS] To: " + user.getEmail() + " (simulate phone) | " + subject + " - " + body);
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
