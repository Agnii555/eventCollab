package edu.neu.csye7374.bridge;

import edu.neu.csye7374.user.User;
import edu.neu.csye7374.event.EventAPI;
import java.util.List;

public interface NotificationSender {
    boolean sendToUser(User user, String subject, String body);
    boolean sendToUsers(List<User> users, String subject, String body);
    boolean sendToEventParticipants(EventAPI event, String subject, String body);
}
