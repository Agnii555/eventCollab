package edu.neu.csye7374.bridge;

import edu.neu.csye7374.user.User;
import edu.neu.csye7374.event.EventAPI;
import java.util.List;

public abstract class NotificationServiceBridge {
    protected NotificationSender sender;
    public NotificationServiceBridge(NotificationSender sender) { this.sender = sender; }
    public void setSender(NotificationSender sender) { this.sender = sender; }

    public abstract boolean notifyUser(User user, String subject, String body);
    public abstract boolean notifyEventParticipants(EventAPI event, String subject, String body);
    public abstract boolean notifyAllUsers(List<User> users, String subject, String body);
}
