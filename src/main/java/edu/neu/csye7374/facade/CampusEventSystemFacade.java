package edu.neu.csye7374.facade;

import edu.neu.csye7374.adapter.ExternalEventAdapter;
import edu.neu.csye7374.bridge.EmailNotificationSender;
import edu.neu.csye7374.bridge.EventNotificationService;
import edu.neu.csye7374.bridge.NotificationSender;
import edu.neu.csye7374.decorator.OnlineStreamingDecorator;
import edu.neu.csye7374.event.EventAPI;
import edu.neu.csye7374.factory.AbstractEventFactory;
import edu.neu.csye7374.factory.LectureEventFactory;
import edu.neu.csye7374.factory.SeminarEventFactory;
import edu.neu.csye7374.factory.WorkshopEventFactory;
import edu.neu.csye7374.manager.EventManager;
import edu.neu.csye7374.service.SearchService;
import edu.neu.csye7374.strategy.SortByDate;
import edu.neu.csye7374.strategy.SortByTitle;
import edu.neu.csye7374.strategy.SortStrategy;
import edu.neu.csye7374.user.Organizer;
import edu.neu.csye7374.user.Student;
import edu.neu.csye7374.user.User;

import java.time.LocalDate;
import java.util.*;

/**
 * Facade pattern implementation with Bridge for notifications.
 */
public class CampusEventSystemFacade {

    private static CampusEventSystemFacade instance;

    // Core subsystems
    private final EventManager eventManager;
    private final EventNotificationService notificationService;
    private final SearchService searchService;

    // Event factories
    private final Map<String, AbstractEventFactory> eventFactories;

    // External adapters
    private final List<ExternalEventAdapter> externalAdapters;

    private CampusEventSystemFacade() {
        // Initialize core subsystems
        this.eventManager = EventManager.getInstance();

        // Bridge: abstraction + default implementor (Email)
        NotificationSender defaultSender = new EmailNotificationSender();
        this.notificationService = new EventNotificationService(defaultSender);

        this.searchService = new SearchService();

        // Initialize event factories
        this.eventFactories = new HashMap<>();
        this.eventFactories.put("lecture", new LectureEventFactory());
        this.eventFactories.put("seminar", new SeminarEventFactory());
        this.eventFactories.put("workshop", new WorkshopEventFactory());

        // Initialize external adapters list
        this.externalAdapters = new ArrayList<>();
    }

    public static CampusEventSystemFacade getInstance() {
        if (instance == null) {
            instance = new CampusEventSystemFacade();
        }
        return instance;
    }

    /**
     * Switch notification channel at runtime (e.g., Email → SMS)
     */
    public void setNotificationSender(NotificationSender sender) {
        this.notificationService.setSender(sender);
    }

    // ==================== USER MANAGEMENT ====================

    public boolean registerStudent(String id, String name, String email,
                                   String studentId, String major, int year) {
        try {
            Student student = new Student(id, name, email, studentId, major, year);
            boolean registered = student.register();
            if (registered) {
                eventManager.registerUser(student);
                notificationService.notifyUser(student,
                        "Welcome to Campus Event System",
                        "Hello " + student.getName() + ", welcome aboard!");
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error registering student: " + e.getMessage());
            return false;
        }
    }

    public boolean registerOrganizer(String id, String name, String email,
                                     String department, String role) {
        try {
            Organizer organizer = new Organizer(id, name, email, department, role);
            boolean registered = organizer.register();
            if (registered) {
                eventManager.registerUser(organizer);
                notificationService.notifyUser(organizer,
                        "Welcome Organizer",
                        "Hello " + organizer.getName() + ", you can now create events.");
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error registering organizer: " + e.getMessage());
            return false;
        }
    }

    public boolean loginUser(String userId) {
        try {
            User user = eventManager.getUser(userId);
            if (user != null) {
                boolean loginSuccess = user.login();
                if (loginSuccess) {
                    notificationService.notifyUser(user,
                            "Login Successful",
                            "Hello " + user.getName() + ", you have successfully logged in.");
                }
                return loginSuccess;
            }
            System.out.println("User not found: " + userId);
            return false;
        } catch (Exception e) {
            System.err.println("Error during user login: " + e.getMessage());
            return false;
        }
    }

    // ==================== EVENT MANAGEMENT ====================

    public String createEvent(String organizerId, String eventType, String title,
                              String description, LocalDate date, String location, int capacity, String streamLink) {
        try {
            User user = eventManager.getUser(organizerId);
            if (!(user instanceof Organizer)) {
                System.out.println("Only organizers can create events");
                return null;
            }

            AbstractEventFactory factory = eventFactories.get(eventType.toLowerCase());
            if (factory == null) {
                System.out.println("Unsupported event type: " + eventType);
                return null;
            }

            String eventId = "EVENT_" + System.currentTimeMillis();
            EventAPI event = factory.createEvent(eventId, title, description, date, location, capacity);

            if(streamLink != null && !streamLink.isEmpty()) {
                event = new OnlineStreamingDecorator(event, "Zoom", streamLink);
            }

            if (eventManager.addEvent(event)) {
                ((Organizer) user).createEvent(eventType, title, description, date.toString(), location, capacity);

                notificationService.sendEventCreatedNotification(event);
                return eventId;
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error creating event: " + e.getMessage());
            return null;
        }
    }

    public boolean registerForEvent(String userId, String eventId) {
        try {
            boolean registered = eventManager.registerUserForEvent(userId, eventId);
            if (registered) {
                User user = eventManager.getUser(userId);
                EventAPI event = eventManager.getEvent(eventId);
                notificationService.sendRegistrationConfirmation(user, event);
            }
            return registered;
        } catch (Exception e) {
            System.err.println("Error registering for event: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelEventRegistration(String userId, String eventId) {
        try {
            User user = eventManager.getUser(userId);
            if (user instanceof Student) {
                boolean cancelled = ((Student) user).cancelEventRegistration(eventId);
                if (cancelled) {
                    EventAPI event = eventManager.getEvent(eventId);
                    notificationService.notifyUser(user,
                            "Event Registration Cancelled",
                            "You have cancelled your registration for " + event.getTitle());
                }
                return cancelled;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error cancelling registration: " + e.getMessage());
            return false;
        }
    }

    // ==================== EVENT SEARCHING & FILTERING ====================

    public List<EventAPI> searchEventsByTitle(String title) {
        try {
            return searchService.searchByTitle(eventManager.getAllEvents(), title);
        } catch (Exception e) {
            System.err.println("Error searching events by title: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<EventAPI> searchEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        try {
            return searchService.searchByDateRange(eventManager.getAllEvents(), startDate, endDate);
        } catch (Exception e) {
            System.err.println("Error searching events by date: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<EventAPI> searchEventsByLocation(String location) {
        try {
            return searchService.searchByLocation(eventManager.getAllEvents(), location);
        } catch (Exception e) {
            System.err.println("Error searching events by location: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<EventAPI> getSortedEvents(String sortBy) {
        try {
            List<EventAPI> events = eventManager.getAllEvents();
            SortStrategy strategy;

            switch (sortBy.toLowerCase()) {
                case "date":
                    strategy = new SortByDate();
                    break;
                case "title":
                    strategy = new SortByTitle();
                    break;
                default:
                    strategy = new SortByDate();
            }

            return strategy.sortEvents(events);
        } catch (Exception e) {
            System.err.println("Error sorting events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==================== NOTIFICATION MANAGEMENT ====================

    public void notifyEventParticipants(String eventId, String message) {
        try {
            EventAPI event = eventManager.getEvent(eventId);
            if (event != null) {
                notificationService.notifyEventParticipants(event, "Event Update", message);
            }
        } catch (Exception e) {
            System.err.println("Error sending notifications: " + e.getMessage());
        }
    }

    public void sendSystemAnnouncement(String message) {
        try {
            List<User> allUsers = eventManager.getAllUsers();
            notificationService.notifyAllUsers(allUsers, "System Announcement", message);
        } catch (Exception e) {
            System.err.println("Error sending system announcement: " + e.getMessage());
        }
    }

    // ==================== EXTERNAL INTEGRATION ====================

    public void addExternalAdapter(ExternalEventAdapter adapter) {
        externalAdapters.add(adapter);
        System.out.println("External adapter added: " + adapter.getClass().getSimpleName());
    }

    public int importExternalEvents() {
        int importedCount = 0;
        try {
            for (ExternalEventAdapter adapter : externalAdapters) {
                List<EventAPI> externalEvents = adapter.importEvents();
                for (EventAPI event : externalEvents) {
                    if (eventManager.addEvent(event)) {
                        importedCount++;
                    }
                }
            }
            System.out.println("Imported " + importedCount + " external events");
        } catch (Exception e) {
            System.err.println("Error importing external events: " + e.getMessage());
        }
        return importedCount;
    }

    // ==================== SYSTEM STATISTICS ====================

    public SystemStats getSystemStatistics() {
        try {
            List<EventAPI> events = eventManager.getAllEvents();
            List<User> users = eventManager.getAllUsers();

            long studentCount = users.stream().filter(u -> u instanceof Student).count();
            long organizerCount = users.stream().filter(u -> u instanceof Organizer).count();

            return new SystemStats(
                    events.size(),
                    users.size(),
                    (int) studentCount,
                    (int) organizerCount,
                    externalAdapters.size()
            );
        } catch (Exception e) {
            System.err.println("Error getting system statistics: " + e.getMessage());
            return new SystemStats(0, 0, 0, 0, 0);
        }
    }

    public List<EventAPI> getUserEvents(String userId) {
        try {
            User user = eventManager.getUser(userId);
            List<EventAPI> userEvents = new ArrayList<>();
            if (user instanceof Student) {
                for (String eventId : ((Student) user).getRegisteredEvents()) {
                    EventAPI event = eventManager.getEvent(eventId);
                    if (event != null) userEvents.add(event);
                }
            } else if (user instanceof Organizer) {
                for (String eventId : ((Organizer) user).getCreatedEvents()) {
                    EventAPI event = eventManager.getEvent(eventId);
                    if (event != null) userEvents.add(event);
                }
            }
            return userEvents;
        } catch (Exception e) {
            System.err.println("Error getting user events: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ==================== ALERT SYSTEM ====================



    public void sendNotificationToUser(String userId, String subject, String message) {
        try {
            System.out.println("🏗️ [FACADE] Starting sendNotificationToUser process...");
            System.out.println("🏗️ [FACADE] User ID: " + userId);
            System.out.println("🏗️ [FACADE] Subject: " + subject);
            System.out.println("🏗️ [FACADE] Message: " + message);
            
            User targetUser = eventManager.getUser(userId);
            if (targetUser == null) {
                System.err.println("❌ [FACADE] User not found in EventManager: " + userId);
                return;
            }
            
            System.out.println("✅ [FACADE] User retrieved from EventManager:");
            System.out.println("   - Name: " + targetUser.getName());
            System.out.println("   - Email: " + targetUser.getEmail());
            System.out.println("   - Type: " + targetUser.getClass().getSimpleName());
            System.out.println("   - ID: " + targetUser.getId());
            
            System.out.println("🔍 [FACADE] Getting notification service instance...");
            System.out.println("🔍 [FACADE] Notification service type: " + notificationService.getClass().getSimpleName());
            
            // Use the notification service to send to specific user
            System.out.println("📤 [FACADE] Calling notificationService.notifyUser()...");
            boolean success = notificationService.notifyUser(targetUser, subject, message);
            
            if (success) {
                System.out.println("✅ [FACADE] Notification service returned SUCCESS for " + targetUser.getName() + "!");
            } else {
                System.out.println("❌ [FACADE] Notification service returned FAILURE for " + targetUser.getName());
            }
            
            System.out.println("🏗️ [FACADE] sendNotificationToUser process completed.");
            
        } catch (Exception e) {
            System.err.println("❌ [FACADE] Exception in sendNotificationToUser: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static class SystemStats {
        private final int totalEvents;
        private final int totalUsers;
        private final int totalStudents;
        private final int totalOrganizers;
        private final int totalExternalAdapters;

        public SystemStats(int totalEvents, int totalUsers, int totalStudents,
                           int totalOrganizers, int totalExternalAdapters) {
            this.totalEvents = totalEvents;
            this.totalUsers = totalUsers;
            this.totalStudents = totalStudents;
            this.totalOrganizers = totalOrganizers;
            this.totalExternalAdapters = totalExternalAdapters;
        }

        public int getTotalEvents() { return totalEvents; }
        public int getTotalUsers() { return totalUsers; }
        public int getTotalStudents() { return totalStudents; }
        public int getTotalOrganizers() { return totalOrganizers; }
        public int getTotalExternalAdapters() { return totalExternalAdapters; }

        @Override
        public String toString() {
            return String.format(
                    "System Statistics:\n" +
                            "- Total Events: %d\n" +
                            "- Total Users: %d\n" +
                            "  - Students: %d\n" +
                            "  - Organizers: %d\n" +
                            "- External Adapters: %d",
                    totalEvents, totalUsers, totalStudents, totalOrganizers, totalExternalAdapters
            );
        }
    }
}
